//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

/**
 * 此包包含与游戏模组相关的类和功能。
 */

import arc.Core;
import arc.assets.AssetDescriptor;
import arc.assets.loaders.SoundLoader;
import arc.audio.Sound;
import arc.files.Fi;
import arc.func.Func;
import arc.func.Prov;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.math.Interp;
import arc.math.geom.Mat3D;
import arc.math.geom.Rect;
import arc.math.geom.Vec3;
import arc.struct.ObjectMap;
import arc.struct.ObjectSet;
import arc.struct.OrderedMap;
import arc.struct.Seq;
import arc.util.I18NBundle;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.Reflect;
import arc.util.Strings;
import arc.util.serialization.Json;
import arc.util.serialization.JsonValue;
import arc.util.serialization.Jval;
import arc.util.serialization.SerializationException;
import arc.util.serialization.Jval.Jformat;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.FlyingAI;
import mindustry.content.Blocks;
import mindustry.content.Bullets;
import mindustry.content.Fx;
import mindustry.content.Loadouts;
import mindustry.content.TechTree;
import mindustry.ctype.Content;
import mindustry.ctype.ContentType;
import mindustry.ctype.MappableContent;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.Effect;
import mindustry.entities.UnitSorts;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.ParticleEffect;
import mindustry.entities.part.DrawPart;
import mindustry.entities.part.RegionPart;
import mindustry.entities.part.DrawPart.PartProgress;
import mindustry.entities.pattern.ShootPattern;
import mindustry.entities.units.UnitController;
import mindustry.game.Objectives;
import mindustry.game.Schematic;
import mindustry.game.Schematics;
import mindustry.game.SpawnGroup;
import mindustry.gen.BuildingTetherPayloadUnit;
import mindustry.gen.CrawlUnit;
import mindustry.gen.ElevationMoveUnit;
import mindustry.gen.LegsUnit;
import mindustry.gen.MechUnit;
import mindustry.gen.PayloadUnit;
import mindustry.gen.Sounds;
import mindustry.gen.TankUnit;
import mindustry.gen.TimedKillUnit;
import mindustry.gen.Unit;
import mindustry.gen.UnitEntity;
import mindustry.gen.UnitWaterMove;
import mindustry.graphics.CacheLayer;
import mindustry.graphics.Shaders;
import mindustry.graphics.g3d.GenericMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MatMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.graphics.g3d.NoiseMesh;
import mindustry.graphics.g3d.ShaderSphereMesh;
import mindustry.graphics.g3d.SunMesh;
import mindustry.graphics.g3d.PlanetGrid.Ptile;
import mindustry.io.SaveVersion;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.maps.planet.AsteroidGenerator;
import mindustry.type.AmmoType;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.type.PayloadStack;
import mindustry.type.Planet;
import mindustry.type.Sector;
import mindustry.type.SectorPreset;
import mindustry.type.StatusEffect;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.Weather;
import mindustry.type.ammo.ItemAmmoType;
import mindustry.type.ammo.PowerAmmoType;
import mindustry.type.weather.ParticleWeather;
import mindustry.world.Block;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeCoolant;
import mindustry.world.consumers.ConsumeItemCharged;
import mindustry.world.consumers.ConsumeItemExplode;
import mindustry.world.consumers.ConsumeItemExplosive;
import mindustry.world.consumers.ConsumeItemFlammable;
import mindustry.world.consumers.ConsumeItemRadioactive;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.consumers.ConsumeLiquidBase;
import mindustry.world.consumers.ConsumeLiquidFlammable;
import mindustry.world.consumers.ConsumeLiquids;
import mindustry.world.consumers.ConsumePower;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawMulti;
import mindustry.world.meta.Attribute;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;

/**
 * ContentParser 类用于解析和创建游戏中的各种内容，如方块、单位、天气等。
 * 它提供了从 JSON 数据中读取内容的功能，并处理不同类型内容的创建和初始化。
 */
public class ContentParser {
    // 是否忽略未知字段
    private static final boolean ignoreUnknownFields = true;
    // 存储类与内容类型的映射
    ObjectMap<Class<?>, ContentType> contentTypes = new ObjectMap();
    // 存储隐式可为空的类
    ObjectSet<Class<?>> implicitNullable = ObjectSet.with(new Class[]{TextureRegion.class, TextureRegion[].class, TextureRegion[][].class, TextureRegion[][][].class});
    // 存储声音资源描述符
    ObjectMap<String, AssetDescriptor<?>> sounds = new ObjectMap();
    // 存储解析监听器
    Seq<ParseListener> listeners = new Seq();
    // 存储类的字段解析器
    ObjectMap<Class<?>, FieldParser> classParsers = new ObjectMap<Class<?>, FieldParser>() {
        {
            /**
             * 为 Effect 类型添加解析器
             * 如果 data 是字符串，从 Fx 类中查找对应的 Effect
             * 如果 data 是数组，创建一个 MultiEffect
             * 否则，根据 type 字段解析并创建一个新的 Effect
             */
            this.put(Effect.class, (type, data) -> {
                if (data.isString()) {
                    return ContentParser.this.field(Fx.class, data);
                } else if (data.isArray()) {
                    return new MultiEffect((Effect[])ContentParser.this.parser.readValue(Effect[].class, data));
                } else {
                    // 解析 Effect 类型
                    Class<? extends Effect> bc = ContentParser.this.resolve(data.getString("type", ""), ParticleEffect.class);
                    data.remove("type");
                    // 创建 Effect 实例
                    Effect result = (Effect)ContentParser.this.make(bc);
                    // 读取 Effect 的字段
                    ContentParser.this.readFields(result, data);
                    return result;
                }
            });
            // 为 Units.Sortf 类型添加解析器，从 UnitSorts 类中查找对应的排序函数
            this.put(Units.Sortf.class, (type, data) -> {
                return ContentParser.this.field(UnitSorts.class, data);
            });
            // 为 Interp 类型添加解析器，从 Interp 类中查找对应的插值函数
            this.put(Interp.class, (type, data) -> {
                return ContentParser.this.field(Interp.class, data);
            });
            // 为 Blending 类型添加解析器，从 Blending 类中查找对应的混合模式
            this.put(Blending.class, (type, data) -> {
                return ContentParser.this.field(Blending.class, data);
            });
            // 为 CacheLayer 类型添加解析器，从 CacheLayer 类中查找对应的缓存层
            this.put(CacheLayer.class, (type, data) -> {
                return ContentParser.this.field(CacheLayer.class, data);
            });
            // 为 Attribute 类型添加解析器，如果属性存在则返回，否则添加新属性
            this.put(Attribute.class, (type, data) -> {
                String attr = data.asString();
                return Attribute.exists(attr) ? Attribute.get(attr) : Attribute.add(attr);
            });
            // 为 BuildVisibility 类型添加解析器，从 BuildVisibility 类中查找对应的可见性
            this.put(BuildVisibility.class, (type, data) -> {
                return ContentParser.this.field(BuildVisibility.class, data);
            });
            // 为 Schematic 类型添加解析器，根据数据类型读取对应的蓝图
            this.put(Schematic.class, (type, data) -> {
                Object result = ContentParser.this.fieldOpt(Loadouts.class, data);
                if (result != null) {
                    return result;
                } else {
                    String str = data.asString();
                    return str.startsWith("bXNjaA") ? Schematics.readBase64(str) : Schematics.read(Vars.tree.get("schematics/" + str + "." + "msch"));
                }
            });
            // 为 Color 类型添加解析器，根据字符串值创建颜色对象
            this.put(Color.class, (type, data) -> {
                return Color.valueOf(data.asString());
            });
            // 为 StatusEffect 类型添加解析器，根据数据创建或查找状态效果
            this.put(StatusEffect.class, (type, data) -> {
                StatusEffect result;
                if (data.isString()) {
                    result = (StatusEffect)ContentParser.this.locate(ContentType.status, data.asString());
                    if (result != null) {
                        return result;
                    } else {
                        throw new IllegalArgumentException("Unknown status effect: '" + data.asString() + "'");
                    }
                } else {
                    result = new StatusEffect(ContentParser.this.currentMod.name + "-" + data.getString("name"));
                    result.minfo.mod = ContentParser.this.currentMod;
                    ContentParser.this.readFields(result, data);
                    return result;
                }
            });
            // 为 UnitCommand 类型添加解析器，根据字符串查找对应的单位命令
            this.put(UnitCommand.class, (type, data) -> {
                if (data.isString()) {
                    UnitCommand cmd = (UnitCommand)UnitCommand.all.find((u) -> {
                        return u.name.equals(data.asString());
                    });
                    if (cmd != null) {
                        return cmd;
                    } else {
                        throw new IllegalArgumentException("Unknown unit command name: " + data.asString());
                    }
                } else {
                    throw new IllegalArgumentException("Unit commands must be strings.");
                }
            });
            // 为 BulletType 类型添加解析器，根据数据创建或查找子弹类型
            this.put(BulletType.class, (type, data) -> {
                if (data.isString()) {
                    return ContentParser.this.field(Bullets.class, data);
                } else {
                    // 解析子弹类型
                    Class<?> bc = ContentParser.this.resolve(data.getString("type", ""), BasicBulletType.class);
                    data.remove("type");
                    // 创建子弹类型实例
                    BulletType result = (BulletType)ContentParser.this.make(bc);
                    // 读取子弹类型的字段
                    ContentParser.this.readFields(result, data);
                    return result;
                }
            });
            // 为 AmmoType 类型添加解析器，根据数据创建或查找弹药类型
            this.put(AmmoType.class, (type, data) -> {
                if (data.isString()) {
                    return new ItemAmmoType((Item)ContentParser.this.find(ContentType.item, data.asString()));
                } else if (data.isNumber()) {
                    return new PowerAmmoType(data.asFloat());
                } else {
                    // 解析弹药类型
                    Class<ItemAmmoType> bc = ContentParser.this.resolve(data.getString("type", ""), ItemAmmoType.class);
                    data.remove("type");
                    // 创建弹药类型实例
                    AmmoType result = (AmmoType)ContentParser.this.make(bc);
                    // 读取弹药类型的字段
                    ContentParser.this.readFields(result, data);
                    return result;
                }
            });
            // 为 DrawBlock 类型添加解析器，根据数据创建或查找方块绘制器
            this.put(DrawBlock.class, (type, data) -> {
                if (data.isString()) {
                    return ContentParser.this.make(ContentParser.this.resolve(data.asString()));
                } else if (data.isArray()) {
                    return new DrawMulti((DrawBlock[])ContentParser.this.parser.readValue(DrawBlock[].class, data));
                } else {
                    // 解析方块绘制器类型
                    Class<DrawDefault> bc = ContentParser.this.resolve(data.getString("type", ""), DrawDefault.class);
                    data.remove("type");
                    // 创建方块绘制器实例
                    DrawBlock result = (DrawBlock)ContentParser.this.make(bc);
                    // 读取方块绘制器的字段
                    ContentParser.this.readFields(result, data);
                    return result;
                }
            });
            // 为 ShootPattern 类型添加解析器，根据数据创建或查找射击模式
            this.put(ShootPattern.class, (type, data) -> {
                // 解析射击模式类型
                Class<ShootPattern> bc = ContentParser.this.resolve(data.getString("type", ""), ShootPattern.class);
                data.remove("type");
                // 创建射击模式实例
                ShootPattern result = (ShootPattern)ContentParser.this.make(bc);
                // 读取射击模式的字段
                ContentParser.this.readFields(result, data);
                return result;
            });
            // 为 DrawPart 类型添加解析器，根据数据创建或查找绘制部分
            this.put(DrawPart.class, (type, data) -> {
                // 解析绘制部分类型
                Class<?> bc = ContentParser.this.resolve(data.getString("type", ""), RegionPart.class);
                data.remove("type");
                // 创建绘制部分实例
                Object result = ContentParser.this.make(bc);
                // 读取绘制部分的字段
                ContentParser.this.readFields(result, data);
                return result;
            });
            // 为 DrawPart.PartProgress 类型添加解析器，根据数据创建或查找绘制部分进度
            this.put(DrawPart.PartProgress.class, (type, data) -> {
                if (data.isString()) {
                    return ContentParser.this.field(DrawPart.PartProgress.class, data.asString());
                } else if (data.isNumber()) {
                    return PartProgress.constant(data.asFloat());
                } else if (!data.has("type")) {
                    throw new RuntimeException("PartProgress object need a 'type' string field. Check the PartProgress class for a list of constants.");
                } else {
                    // 获取基础的绘制部分进度
                    DrawPart.PartProgress base = (DrawPart.PartProgress)ContentParser.this.field(DrawPart.PartProgress.class, data.getString("type"));
                    // 获取操作值
                    JsonValue opval = data.has("operation") ? data.get("operation") : (data.has("op") ? data.get("op") : null);
                    if (opval == null) {
                        // 获取操作数组
                        JsonValue opsVal = data.has("operations") ? data.get("operations") : (data.has("ops") ? data.get("ops") : null);
                        if (opsVal != null) {
                            if (!opsVal.isArray()) {
                                throw new RuntimeException("Chained PartProgress operations must be an array.");
                            }

                            int i = 0;

                            while(true) {
                                JsonValue val = opsVal.get(i);
                                if (val == null) {
                                    break;
                                }

                                JsonValue op = val.has("operation") ? val.get("operation") : (val.has("op") ? val.get("op") : null);
                                // 解析进度操作
                                base = ContentParser.this.parseProgressOp(base, op.asString(), val);
                                ++i;
                            }
                        }

                        return base;
                    } else {
                        String opx = opval.asString();
                        // 解析进度操作
                        return ContentParser.this.parseProgressOp(base, opx, data);
                    }
                }
            });
            // 为 PlanetGenerator 类型添加解析器，创建一个小行星生成器
            this.put(PlanetGenerator.class, (type, data) -> {
                AsteroidGenerator result = new AsteroidGenerator();
                ContentParser.this.readFields(result, data);
                return result;
            });
            // 为 Mat3D 类型添加解析器，根据数据创建或修改矩阵
            this.put(Mat3D.class, (type, data) -> {
                if (data == null) {
                    return new Mat3D();
                } else if (data.has("x") && data.has("y") && data.has("z")) {
                    return (new Mat3D()).translate(data.getFloat("x", 0.0F), data.getFloat("y", 0.0F), data.getFloat("z", 0.0F));
                } else if (data.isArray() && data.size == 3) {
                    return (new Mat3D()).setToTranslation(new Vec3(data.asFloatArray()));
                } else {
                    Mat3D mat = new Mat3D();
                    JsonValue.JsonIterator var4 = data.iterator();

                    while(var4.hasNext()) {
                        JsonValue val = (JsonValue)var4.next();
                        switch (val.name) {
                            case "translate":
                            case "trans":
                                mat.translate((Vec3)ContentParser.this.parser.readValue(Vec3.class, data));
                                break;
                            case "scale":
                            case "scl":
                                mat.scale((Vec3)ContentParser.this.parser.readValue(Vec3.class, data));
                                break;
                            case "rotate":
                            case "rot":
                                mat.rotate((Vec3)ContentParser.this.parser.readValue(Vec3.class, data), data.getFloat("degrees", 0.0F));
                                break;
                            case "multiply":
                            case "mul":
                                mat.mul((Mat3D)ContentParser.this.parser.readValue(Mat3D.class, data));
                            case "x":
                            case "y":
                            case "z":
                                break;
                            default:
                                throw new RuntimeException("Unknown matrix transformation: '" + val.name + "'");
                        }
                    }

                    return mat;
                }
            });
            // 为 Vec3 类型添加解析器，根据数据创建三维向量
            this.put(Vec3.class, (type, data) -> {
                return data.isArray() ? new Vec3(data.asFloatArray()) : new Vec3(data.getFloat("x", 0.0F), data.getFloat("y", 0.0F), data.getFloat("z", 0.0F));
            });
            // 为 Sound 类型添加解析器，根据数据加载或获取声音资源
            this.put(Sound.class, (type, data) -> {
                if (ContentParser.this.fieldOpt(Sounds.class, data) != null) {
                    return ContentParser.this.fieldOpt(Sounds.class, data);
                } else if (Vars.headless) {
                    return new Sound();
                } else {
                    String name = "sounds/" + data.asString();
                    String path = Vars.tree.get(name + ".ogg").exists() ? name + ".ogg" : name + ".mp3";
                    if (ContentParser.this.sounds.containsKey(path)) {
                        return ((SoundLoader.SoundParameter)((AssetDescriptor)ContentParser.this.sounds.get(path)).params).sound;
                    } else {
                        Sound sound = new Sound();
                        AssetDescriptor<?> desc = Core.assets.load(path, Sound.class, new SoundLoader.SoundParameter(sound));
                        desc.errored = Throwable::printStackTrace;
                        ContentParser.this.sounds.put(path, desc);
                        return sound;
                    }
                }
            });
            // 为 Objectives.Objective 类型添加解析器，根据数据创建或查找目标
            this.put(Objectives.Objective.class, (type, data) -> {
                if (data.isString()) {
                    MappableContent cont = ContentParser.this.locateAny(data.asString());
                    if (cont == null) {
                        throw new IllegalArgumentException("Unknown objective content: " + data.asString());
                    } else {
                        return new Objectives.Research((UnlockableContent)cont);
                    }
                } else {
                    // 解析目标类型
                    Class<Objectives.SectorComplete> oc = ContentParser.this.resolve(data.getString("type", ""), Objectives.SectorComplete.class);
                    data.remove("type");
                    // 创建目标实例
                    Objectives.Objective obj = (Objectives.Objective)ContentParser.this.make(oc);
                    // 读取目标的字段
                    ContentParser.this.readFields(obj, data);
                    return obj;
                }
            });
            // 为 Ability 类型添加解析器，根据数据创建或查找能力
            this.put(Ability.class, (type, data) -> {
                // 解析能力类型
                Class<? extends Ability> oc = ContentParser.this.resolve(data.getString("type", ""));
                data.remove("type");
                // 创建能力实例
                Ability obj = (Ability)ContentParser.this.make(oc);
                // 读取能力的字段
                ContentParser.this.readFields(obj, data);
                return obj;
            });
            // 为 Weapon 类型添加解析器，根据数据创建或查找武器
            this.put(Weapon.class, (type, data) -> {
                // 解析武器类型
                Class<Weapon> oc = ContentParser.this.resolve(data.getString("type", ""), Weapon.class);
                data.remove("type");
                // 创建武器实例
                Weapon weapon = (Weapon)ContentParser.this.make(oc);
                // 读取武器的字段
                ContentParser.this.readFields(weapon, data);
                weapon.name = ContentParser.this.currentMod.name + "-" + weapon.name;
                return weapon;
            });
            // 为 Consume 类型添加解析器，根据数据创建或查找消耗器
            this.put(Consume.class, (type, data) -> {
                // 解析消耗器类型
                Class<Consume> oc = ContentParser.this.resolve(data.getString("type", ""), Consume.class);
                data.remove("type");
                // 创建消耗器实例
                Consume consume = (Consume)ContentParser.this.make(oc);
                // 读取消耗器的字段
                ContentParser.this.readFields(consume, data);
                return consume;
            });
            // 为 ConsumeLiquidBase 类型添加解析器，根据数据创建或查找液体消耗器
            this.put(ConsumeLiquidBase.class, (type, data) -> {
                // 解析液体消耗器类型
                Class<ConsumeLiquidBase> oc = ContentParser.this.resolve(data.getString("type", ""), ConsumeLiquidBase.class);
                data.remove("type");
                // 创建液体消耗器实例
                ConsumeLiquidBase consume = (ConsumeLiquidBase)ContentParser.this.make(oc);
                // 读取液体消耗器的字段
                ContentParser.this.readFields(consume, data);
                return consume;
            });
        }
    };
    // 存储待执行的读取操作
    private Seq<Runnable> reads = new Seq();
    // 存储读取后待执行的操作
    private Seq<Runnable> postreads = new Seq();
    // 存储待解析的对象
    private ObjectSet<Object> toBeParsed = new ObjectSet();
    // 当前处理的模组
    Mods.LoadedMod currentMod;
    // 当前处理的内容
    Content currentContent;
    // JSON 解析器
    private Json parser = new Json() {
        /**
         * 重写 readValue 方法，用于读取 JSON 数据并创建对象
         * 检查对象的空字段，并调用解析监听器
         */
        public <T> T readValue(Class<T> type, Class elementType, JsonValue jsonData, Class keyType) {
            T t = this.internalRead(type, elementType, jsonData, keyType);
            if (t != null && !Reflect.isWrapper(t.getClass()) && (type == null || !type.isPrimitive())) {
                // 检查对象的空字段
                ContentParser.this.checkNullFields(t);
                // 调用解析监听器
                ContentParser.this.listeners.each((hook) -> {
                    hook.parsed(type, jsonData, t);
                });
            }

            return t;
        }

        /**
         * 内部读取方法，根据类型和 JSON 数据创建对象
         * 处理特殊类型的解析，如整数位域、物品栈等
         */
        private <T> T internalRead(Class<T> type, Class elementType, JsonValue jsonData, Class keyType) {
            if (type != null) {
                if (ContentParser.this.classParsers.containsKey(type)) {
                    try {
                        return ((FieldParser)ContentParser.this.classParsers.get(type)).parse(type, jsonData);
                    } catch (Exception var9) {
                        Exception e = var9;
                        throw new RuntimeException(e);
                    }
                }

                if ((type == Integer.TYPE || type == Integer.class) && jsonData.isArray()) {
                    int value = 0;

                    String field;
                    for(JsonValue.JsonIterator var13 = jsonData.iterator(); var13.hasNext(); value |= (Integer)Reflect.get(Env.class, field)) {
                        JsonValue str = (JsonValue)var13.next();
                        if (!str.isString()) {
                            throw new SerializationException("Integer bitfield values must all be strings. Found: " + str);
                        }

                        field = str.asString();
                    }

                    return value;
                }

                String[] split;
                if (type == ItemStack.class && jsonData.isString() && jsonData.asString().contains("/")) {
                    split = jsonData.asString().split("/");
                    return this.fromJson(ItemStack.class, "{item: " + split[0] + ", amount: " + split[1] + "}");
                }

                if (type == PayloadStack.class && jsonData.isString() && jsonData.asString().contains("/")) {
                    split = jsonData.asString().split("/");
                    int number = Strings.parseInt(split[1], 1);
                    UnlockableContent cont = Vars.content.unit(split[0]) == null ? Vars.content.block(split[0]) : Vars.content.unit(split[0]);
                    return new PayloadStack((UnlockableContent)(cont == null ? Blocks.router : cont), number);
                }

                if (jsonData.isString() && jsonData.asString().contains("/")) {
                    split = jsonData.asString().split("/");
                    if (type == LiquidStack.class) {
                        return this.fromJson(LiquidStack.class, "{liquid: " + split[0] + ", amount: " + split[1] + "}");
                    }

                    if (type == ConsumeLiquid.class) {
                        return this.fromJson(ConsumeLiquid.class, "{liquid: " + split[0] + ", amount: " + split[1] + "}");
                    }
                }

                if (type == Rect.class && jsonData.isArray() && jsonData.size == 4) {
                    return new Rect(jsonData.get(0).asFloat(), jsonData.get(1).asFloat(), jsonData.get(2).asFloat(), jsonData.get(3).asFloat());
                }

                if (Content.class.isAssignableFrom(type)) {
                    ContentType ctype = (ContentType)ContentParser.this.contentTypes.getThrow(type, () -> {
                        return new IllegalArgumentException("No content type for class: " + type.getSimpleName());
                    });
                    String prefix = ContentParser.this.currentMod != null ? ContentParser.this.currentMod.name + "-" : "";
                    T one = Vars.content.getByName(ctype, prefix + jsonData.asString());
                    if (one != null) {
                        return one;
                    }

                    T two = Vars.content.getByName(ctype, jsonData.asString());
                    if (two != null) {
                        return two;
                    }

                    throw new IllegalArgumentException("\"" + jsonData.name + "\": No " + ctype + " found with name '" + jsonData.asString() + "'.\nMake sure '" + jsonData.asString() + "' is spelled correctly, and that it really exists!\nThis may also occur because its file failed to parse.");
                }
            }

            return super.readValue(type, elementType, jsonData, keyType);
        }
    };
    // 存储不同内容类型的解析器
    private ObjectMap<ContentType, TypeParser<?>> parsers;

    /**
     * 构造函数，初始化内容解析器
     * 为不同的内容类型（如方块、单位、天气等）注册解析器
     */
    public ContentParser() {
        this.parsers = ObjectMap.of(new Object[]{
                // 方块类型解析器
                ContentType.block, (mod, name, value) -> {
            this.readBundle(ContentType.block, name, value);
            Block block;
            if (this.locate(ContentType.block, name) != null) {
                if (value.has("type")) {
                    Log.warn("Warning: '" + this.currentMod.name + "-" + name + "' re-declares a type. This will be interpreted as a new block. If you wish to override a vanilla block, omit the 'type' section, as vanilla block `type`s cannot be changed.", new Object[0]);
                    block = (Block)this.make(this.resolve(value.getString("type", ""), Block.class), mod + "-" + name);
                } else {
                    block = (Block)this.locate(ContentType.block, name);
                }
            } else {
                block = (Block)this.make(this.resolve(value.getString("type", ""), Block.class), mod + "-" + name);
            }

            this.currentContent = block;
            this.read(() -> {
                if (value.has("consumes") && value.get("consumes").isObject()) {
                    JsonValue.JsonIterator var3 = value.get("consumes").iterator();

                    while(var3.hasNext()) {
                        JsonValue child = (JsonValue)var3.next();
                        switch (child.name) {
                            case "item":
                                block.consumeItem((Item)this.find(ContentType.item, child.asString()));
                                break;
                            case "itemCharged":
                                block.consume((Consume)this.parser.readValue(ConsumeItemCharged.class, child));
                                break;
                            case "itemFlammable":
                                block.consume((Consume)this.parser.readValue(ConsumeItemFlammable.class, child));
                                break;
                            case "itemRadioactive":
                                block.consume((Consume)this.parser.readValue(ConsumeItemRadioactive.class, child));
                                break;
                            case "itemExplosive":
                                block.consume((Consume)this.parser.readValue(ConsumeItemExplosive.class, child));
                                break;
                            case "itemExplode":
                                block.consume((Consume)this.parser.readValue(ConsumeItemExplode.class, child));
                                break;
                            case "items":
                                block.consume(child.isArray() ? new ConsumeItems((ItemStack[])this.parser.readValue(ItemStack[].class, child)) : (ConsumeItems)this.parser.readValue(ConsumeItems.class, child));
                                break;
                            case "liquidFlammable":
                                block.consume((Consume)this.parser.readValue(ConsumeLiquidFlammable.class, child));
                                break;
                            case "liquid":
                                block.consume((Consume)this.parser.readValue(ConsumeLiquid.class, child));
                                break;
                            case "liquids":
                                block.consume(child.isArray() ? new ConsumeLiquids((LiquidStack[])this.parser.readValue(LiquidStack[].class, child)) : (ConsumeLiquids)this.parser.readValue(ConsumeLiquids.class, child));
                                break;
                            case "coolant":
                                block.consume((Consume)this.parser.readValue(ConsumeCoolant.class, child));
                                break;
                            case "power":
                                if (child.isNumber()) {
                                    block.consumePower(child.asFloat());
                                } else {
                                    block.consume((Consume)this.parser.readValue(ConsumePower.class, child));
                                }
                                break;
                            case "powerBuffered":
                                block.consumePowerBuffered(child.asFloat());
                                break;
                            default:
                                throw new IllegalArgumentException("Unknown consumption type: '" + child.name + "' for block '" + block.name + "'.");
                        }
                    }

                    value.remove("consumes");
                }

                this.readFields(block, value, true);
                if (block.size > 16) {
                    throw new IllegalArgumentException("Blocks cannot be larger than 16");
                } else {
                    if (value.has("requirements") && block.buildVisibility == BuildVisibility.hidden) {
                        block.buildVisibility = BuildVisibility.shown;
                    }

                }
            });
            return block;
        },
                // 单位类型解析器
                ContentType.unit, (mod, name, value) -> {
            this.readBundle(ContentType.unit, name, value);
            UnitType unit;
            if (this.locate(ContentType.unit, name) == null) {
                unit = (UnitType)this.make(this.resolve(value.getString("template", ""), UnitType.class), mod + "-" + name);
                if (value.has("template")) {
                    value.remove("template");
                }

                JsonValue typeVal = value.get("type");
                if (unit.constructor == null || typeVal != null) {
                    if (typeVal != null && !typeVal.isString()) {
                        throw new RuntimeException("Unit '" + name + "' has an incorrect type. Types must be strings.");
                    }

                    unit.constructor = this.unitType(typeVal);
                }
            } else {
                unit = (UnitType)this.locate(ContentType.unit, name);
            }

            this.currentContent = unit;
            this.read(() -> {
                JsonValue waves;
                if (value.has("requirements")) {
                    waves = value.remove("requirements");
                    UnitReq req = (UnitReq)this.parser.readValue(UnitReq.class, waves);
                    Block patt23136$temp = req.block;
                    if (patt23136$temp instanceof Reconstructor) {
                        Reconstructor r = (Reconstructor)patt23136$temp;
                        if (req.previous != null) {
                            r.upgrades.add(new UnitType[]{req.previous, unit});
                        }
                    } else {
                        patt23136$temp = req.block;
                        if (!(patt23136$temp instanceof UnitFactory)) {
                            throw new IllegalArgumentException("Missing a valid 'block' in 'requirements'");
                        }

                        UnitFactory f = (UnitFactory)patt23136$temp;
                        f.plans.add(new UnitFactory.UnitPlan(unit, req.time, req.requirements));
                    }
                }

                if (value.has("controller") || value.has("aiController")) {
                    unit.aiController = this.supply(this.resolve(value.getString("controller", value.getString("aiController", "")), FlyingAI.class));
                    value.remove("controller");
                }

                if (value.has("defaultController")) {
                    Prov<FlyingAI> sup = this.supply(this.resolve(value.getString("defaultController"), FlyingAI.class));
                    unit.controller = (u) -> {
                        return (UnitController)sup.get();
                    };
                    value.remove("defaultController");
                }

                if (value.has("waves")) {
                    waves = value.remove("waves");
                    SpawnGroup[] groups = (SpawnGroup[])this.parser.readValue(SpawnGroup[].class, waves);
                    SpawnGroup[] var11 = groups;
                    int var12 = groups.length;

                    for(int var13 = 0; var13 < var12; ++var13) {
                        SpawnGroup group = var11[var13];
                        group.type = unit;
                    }

                    Vars.waves.get().addAll(groups);
                }

                this.readFields(unit, value, true);
            });
            return unit;
        },
                // 天气类型解析器
                ContentType.weather, (mod, name, value) -> {
            Weather item;
            if (this.locate(ContentType.weather, name) != null) {
                item = (Weather)this.locate(ContentType.weather, name);
                this.readBundle(ContentType.weather, name, value);
            } else {
                this.readBundle(ContentType.weather, name, value);
                item = (Weather)this.make(this.resolve(this.getType(value), ParticleWeather.class), mod + "-" + name);
                value.remove("type");
            }

            this.currentContent = item;
            this.read(() -> {
                this.readFields(item, value);
            });
            return item;
        },
                // 物品类型解析器
                ContentType.item, this.parser(ContentType.item, Item::new),
                // 液体类型解析器
                ContentType.liquid, (mod, name, value) -> {
            Liquid liquid;
            if (this.locate(ContentType.liquid, name) != null) {
                liquid = (Liquid)this.locate(ContentType.liquid, name);
                this.readBundle(ContentType.liquid, name, value);
            } else {
                this.readBundle(ContentType.liquid, name, value);
                liquid = (Liquid)this.make(this.resolve(value.getString("type", (String)null), Liquid.class), mod + "-" + name);
                value.remove("type");
            }

            this.currentContent = liquid;
            this.read(() -> {
                this.readFields(liquid, value);
            });
            return liquid;
        },
                // 状态效果类型解析器
                ContentType.status, this.parser(ContentType.status, StatusEffect::new),
                // 扇区类型解析器
                ContentType.sector, (mod, name, value) -> {
            if (value.isString()) {
                return (SectorPreset)this.locate(ContentType.sector, name);
            } else if (value.has("sector") && value.get("sector").isNumber()) {
                SectorPreset out = new SectorPreset(mod + "-" + name, this.currentMod);
                this.currentContent = out;
                this.read(() -> {
                    Planet planet = (Planet)this.locate(ContentType.planet, value.getString("planet", "serpulo"));
                    if (planet == null) {
                        throw new RuntimeException("Planet '" + value.getString("planet") + "' not found.");
                    } else {
                        out.initialize(planet, value.getInt("sector", 0));
                        value.remove("sector");
                        value.remove("planet");
                        this.readFields(out, value);
                    }
                });
                return out;
            } else {
                throw new RuntimeException("SectorPresets must have a sector number.");
            }
        },
                // 行星类型解析器
                ContentType.planet, (mod, name, value) -> {
            if (value.isString()) {
                return (Planet)this.locate(ContentType.planet, name);
            } else {
                Planet parent = (Planet)this.locate(ContentType.planet, value.getString("parent"));
                Planet planet = new Planet(mod + "-" + name, parent, value.getFloat("radius", 1.0F), value.getInt("sectorSize", 0));
                JsonValue mesh;
                if (value.has("mesh")) {
                    mesh = value.get("mesh");
                    if (!mesh.isObject() && !mesh.isArray()) {
                        throw new RuntimeException("Meshes must be objects.");
                    }

                    value.remove("mesh");
                    planet.meshLoader = () -> {
                        try {
                            return this.parseMesh(planet, mesh);
                        } catch (Exception var4) {
                            Exception e = var4;
                            Log.err(e);
                            return new ShaderSphereMesh(planet, Shaders.unlit, 2);
                        }
                    };
                }

                if (value.has("cloudMesh")) {
                    mesh = value.get("cloudMesh");
                    if (!mesh.isObject() && !mesh.isArray()) {
                        throw new RuntimeException("Meshes must be objects.");
                    }

                    value.remove("cloudMesh");
                    planet.cloudMeshLoader = () -> {
                        try {
                            return this.parseMesh(planet, mesh);
                        } catch (Exception var4) {
                            Exception e = var4;
                            Log.err(e);
                            return null;
                        }
                    };
                }

                planet.sectors.add(new Sector(planet, Ptile.empty));
                this.currentContent = planet;
                this.read(() -> {
                    this.readFields(planet, value);
                });
                return planet;
            }
        }
        });
    }

    /**
     * 根据 JSON 值确定单位类型的构造函数
     * @param value JSON 值，可能包含单位类型信息
     * @return 单位类型的构造函数提供者
     */
    private Prov<Unit> unitType(JsonValue value) {
        if (value == null) {
            return UnitEntity::create;
        } else {
            Prov var10000;
            switch (value.asString()) {
                case "flying":