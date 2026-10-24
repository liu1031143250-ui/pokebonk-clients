# PokéBonk 全平台客户端

把网页游戏 [pokebonk.com](https://pokebonk.com)（3D 宝可梦生存游戏，类吸血鬼幸存者）封装成的**手机原生客户端**——没有浏览器地址栏、工具栏和标签页，打开就是全屏横屏游戏。一个仓库包含安卓和 iOS 两个平台的完整源码与构建流程。

## 下载安装

到 [Releases](https://github.com/liu1031143250-ui/pokebonk-clients/releases/latest) 下载：

| 平台 | 文件 | 大小 | 系统要求 | 说明 |
|---|---|---|---|---|
| 安卓 | `PokebonkClient-v1.0.apk` | 913 KB | Android 7.0+ (minSdk 24) | 已签名，直接安装 |
| iOS | `PokebonkClient-iOS-unsigned.ipa` | 1.5 MB | iOS 15.0+，iPhone / iPad | **未签名**，需 AltStore / Sideloadly + 免费 Apple ID 侧载（7 天有效，到期重签） |

**安卓安装**：APK 传到手机 → 点击 → 允许"未知来源"一次 → 桌面出现 PokéBonk 图标。

**iOS 安装**：见 [ios/README.md](ios/README.md) 的侧载教程（AltStore / Sideloadly 二选一）。

> 不想折腾 iOS 签名？Safari 打开 `pokebonk.com` → 分享 → **添加到主屏幕**，官网自带全屏 PWA 清单，效果接近原生且永久有效。

## 目录结构

```
pokebonk-clients/
├── android/                  # 安卓客户端（Java + Gradle，零第三方依赖）
│   ├── app/src/main/         #   全部源码与资源（MainActivity / 布局 / 图标 / 主题）
│   ├── app/build.gradle      #   AGP 9.4.1，minSdk 24 / targetSdk 34，条件式签名
│   └── README.md             #   安卓构建与改包教程
├── ios/                      # iOS 客户端（Swift + UIKit + WKWebView）
│   ├── PokebonkClient/       #   AppDelegate / MainViewController / ErrorView / 资源
│   ├── project.yml           #   XcodeGen 工程描述
│   ├── .github/workflows/    #   GitHub Actions macOS 云构建（产出未签名 IPA）
│   └── README.md             #   iOS 侧载安装与构建教程
└── .github/                  # （预留）其他自动化流程
```

## 客户端特性（两端一致）

- **全屏沉浸**：系统栏全部隐藏，横屏锁定，画面铺满整屏（刘海屏适配）
- **屏幕常量**：玩游戏时屏幕不会自动熄屏
- **无浏览器 UI**：没有地址栏、菜单、多标签，误触最小化
- **返回优化**：有网页历史先回退；没有则退到后台，不误杀正在跑的一局
- **断网提示**：网络不通时显示"重新加载"按钮，而不是白屏
- **外链接管**：游戏里的站外链接自动交给系统浏览器
- **冷启动进度条**：顶部红色进度条

安卓额外能力：localStorage 存档与浏览器互通；iOS 额外能力：左滑返回手势。

## 自己构建

**安卓**（详见 [android/README.md](android/README.md)）：

```bash
cd android
export ANDROID_HOME=/path/to/android-sdk   # Android SDK Platform 34
gradle assembleRelease
```

**iOS**（详见 [ios/README.md](ios/README.md)）：

```bash
brew install xcodegen
cd ios && xcodegen generate
# Xcode 打开 PokebonkClient.xcodeproj，选你的 Team，Archive 导出
```

iOS 的 GitHub Actions 云构建：改完 `ios/` 里的代码 push 后，到仓库 Actions 页手动跑 "Build iOS IPA (unsigned)"，产出未签名 IPA。

## 备注

- 游戏本体是官方网页版，客户端只是"全屏外壳"：游戏更新自动生效，无需重新装客户端。
- PokéBonk 是非官方粉丝游戏（Fan-game），免费游玩；本仓库仅做本地封装，未修改游戏内容。
- 安卓签名 keystore 不在仓库里（安全），需要时按 android/README.md 的说明自行生成。
