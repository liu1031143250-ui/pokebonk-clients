# PokéBonk iOS 客户端（Xcode 源码工程 + 云端构建流程）

把网页游戏 [pokebonk.com](https://pokebonk.com) 封装的 **iOS 客户端**（Swift + UIKit + WKWebView）。

> ⚠️ **先说清楚一件事**：苹果规定所有 IPA 必须用 Apple 证书签名才能安装。
> 没有付费开发者账号的情况下，能得到的是 **未签名 IPA**，需要用 AltStore / Sideloadly
> 配合免费 Apple ID 重新签名后才能安装（签名有效期 7 天，到期重签）。
> 这是 iOS 封闭生态的限制，任何"网页套壳客户端"都一样，不是本工程的问题。

## 文件说明

| 文件 | 说明 |
|---|---|
| `PokebonkClient-iOS-unsigned.ipa` | **未签名 IPA**（1.5 MB，iOS 15.0+，iPhone/iPad），已用 GitHub macOS 云构建机编译产出 |
| `pokebonk-ios/` | 完整源码工程 + 云端构建流程 |

IPA 校验（SHA-256）：`65f6a42f4b05ac1d82a9975674e18d08f852aaf95161785e2816c1ada6fc1ca5`

云端构建仓库：https://github.com/liu1031143250-ui/pokebonk-clients（本仓库 `ios/` 目录）
（GitHub Actions 自动编译，改代码后可在仓库里手动重跑 workflow 出新 IPA）

## 工程结构

```
pokebonk-ios/
├── project.yml                    # XcodeGen 工程描述（CI 里用它生成 .xcodeproj）
├── PokebonkClient/
│   ├── AppDelegate.swift          # 入口，window + 根控制器
│   ├── MainViewController.swift   # 全屏 WKWebView + 断网提示 + 外链接管
│   ├── ErrorView.swift            # 加载失败面板
│   ├── Info.plist                 # 横屏锁定 / 隐藏状态栏 / 深色启动屏
│   ├── PokebonkClient.entitlements
│   └── Assets.xcassets/           # App 图标 / 启动屏颜色 / 图标资源
└── .github/workflows/ios.yml      # GitHub Actions macOS 云构建（产出未签名 IPA）
```

## 功能特性

- 全屏 WKWebView，锁定横屏，隐藏状态栏和 Home 指示条
- 玩游戏时 `isIdleTimerDisabled` 屏幕常量
- 左滑手势返回上一页（`allowsBackForwardNavigationGestures`）
- 断网/加载失败显示"重新加载"面板
- 游戏内站外链接自动交给 Safari；`target=_blank` 在本页内打开
- 顶部红色加载进度条
- 部署目标 iOS 15.0+，支持 iPhone / iPad

## 安装未签名 IPA 的方法

### 方式一：AltStore（推荐，免费）

1. Mac/PC 下载 [AltServer](https://altstore.io/)，手机装 AltStore（用免费 Apple ID 登录）
2. 把 `PokebonkClient-unsigned.ipa` 传到手机，用 AltStore 打开 → 安装
3. 之后每周连一次电脑续签（免费 Apple ID 的限制）

### 方式二：Sideloadly（免费，Mac/Windows）

1. 电脑安装 [Sideloadly](https://sideloadly.io/)
2. 手机连接电脑，拖入 IPA，登录免费 Apple ID 即可安装

### 方式三：自己有开发者账号

用开发者证书重签（`codesign` / Xcode Organizer / `fastlane sigh`），或把
`DEVELOPMENT_TEAM` 设成自己的 Team ID 后直接用 Xcode 打包。

## 不想折腾签名？用 PWA

Safari 打开 `pokebonk.com` → 分享 → **添加到主屏幕**，官网自带全屏 PWA 清单，
效果接近原生客户端，且永久有效、无需签名。安卓同理（安卓还可以直接装隔壁那个 APK）。

## 在自己 Mac 上构建

```bash
brew install xcodegen
xcodegen generate
open PokebonkClient.xcodeproj
# Xcode 里选中你的 Team，Product → Archive → Export
```

或在 Xcode 里把 `DEVELOPMENT_TEAM` 改成你的 Team ID 后命令行归档导出。

## 常用改动

| 需求 | 改哪里 |
|---|---|
| 换游戏地址 | `MainViewController.swift` 的 `gameURL` |
| 竖屏 | `Info.plist` 的 `UISupportedInterfaceOrientations` + `MainViewController` 的 `supportedInterfaceOrientations` |
| 换图标 | `Assets.xcassets/AppIcon.appiconset/Icon-1024.png` |
| 改 Bundle ID | `project.yml` 的 `PRODUCT_BUNDLE_IDENTIFIER` |
