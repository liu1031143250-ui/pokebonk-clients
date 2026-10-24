# PokéBonk 安卓客户端

把网页游戏 [pokebonk.com](https://pokebonk.com)（3D 宝可梦生存游戏，类吸血鬼幸存者）封装成的**安卓原生客户端**——没有浏览器地址栏、工具栏和标签页，打开就是全屏横屏游戏。

零第三方依赖，纯 Android 框架 API 实现。

## 下载安装

到 [Releases](https://github.com/liu1031143250-ui/pokebonk-clients/releases/latest) 下载 `PokebonkClient-v1.0.apk`（913 KB，安卓 7.0+ / minSdk 24）。

1. 把 APK 传到手机（数据线 / 微信文件传输 / QQ 等均可）。
2. 点击 APK，系统提示"禁止安装未知应用"时，按指引允许一次。
3. 安装完成后桌面出现 **PokéBonk** 图标，点开即玩。

> 已用自签名证书签名，仅用于本机分发；不申请任何敏感权限（只要了一个 INTERNET 联网权限）。游戏存档在应用私有目录里，卸载才会丢。

## 客户端特性

- **全屏沉浸**：状态栏、导航栏全部隐藏，横屏锁定，画面铺满整屏（刘海屏也做了适配）
- **屏幕常量**：玩游戏时屏幕不会自动熄屏
- **无浏览器 UI**：没有地址栏、菜单、多标签，误触最小化
- **返回键优化**：有网页历史先回退；没有则退到后台，不会误触直接杀掉正在跑的一局
- **断网提示**：网络不通时显示"重新加载"按钮，而不是白屏
- **外链接管**：游戏里的外部链接自动交给系统浏览器打开
- **进度条**：冷启动加载时有顶部红色进度条
- **独立运行环境**：localStorage / 存档与浏览器互通无阻塞，后台切回不刷新页面

## 自己改 / 重新打包

```bash
git clone https://github.com/liu1031143250-ui/pokebonk-clients.git
cd pokebonk-clients/android
export ANDROID_HOME=/path/to/android-sdk   # 需要 Android SDK Platform 34
gradle assembleRelease
```

产物在 `app/build/outputs/apk/release/app-release.apk`。

**生成自己的签名证书**（可选，不生成则产出未签名包）：

```bash
keytool -genkeypair -v -keystore app/pokebonk.keystore \
    -alias pokebonk -keyalg RSA -keysize 2048 -validity 10000
```

把生成的 keystore 放成 `app/pokebonk.keystore`，`app/build.gradle` 会在检测到它时自动启用签名（密码在 `signingConfigs.release` 里按需修改）。

常见修改点：

- 改游戏地址：`app/src/main/java/com/pokebonk/client/MainActivity.java` 里的 `GAME_URL`
- 改应用名/包名：`res/values/strings.xml` 和 `app/build.gradle` 的 `applicationId`
- 改图标：替换 `app/src/main/res/mipmap-*` 下的 PNG 和 `drawable/ic_launcher_foreground.png`
- 换成竖屏：`AndroidManifest.xml` 里 `android:screenOrientation="sensorLandscape"` 改成 `portrait`

## iPhone / iPad 怎么办

iOS 没法装 APK，但 PokéBonk 官网自带全屏 PWA 清单，用 Safari"添加到主屏幕"后同样是全屏无地址栏的体验：

1. Safari 打开 `pokebonk.com`
2. 分享按钮 → **添加到主屏幕** → 添加
3. 之后从桌面的 PokéBonk 图标进入即为全屏横屏游戏

（iOS 封装客户端的源码在同仓库的 [../ios](../ios) 目录。）

## 备注

- 游戏本体是官方网页版，客户端只是"全屏外壳"：游戏更新自动生效，无需重新装客户端。
- PokéBonk 是非官方粉丝游戏（Fan-game），免费游玩；本客户端仅做本地封装，未修改游戏内容。
