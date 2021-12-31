
# ArchNg

主要用于 App的开发使用。
跟随Google的步伐。

# 以`git-submodule`接入

[Git子模块使用参考](https://git-scm.com/book/zh/v2/Git-%E5%B7%A5%E5%85%B7-%E5%AD%90%E6%A8%A1%E5%9D%97)

## clone出，已经引入submodule的repo

假如， 已引入submodule的项目是 `HostApp`

```bash
git clone ssh://host.repo.git  HostApp

cd HostApp

git submodule update --init --recursive
```

第一次拉取repo后，进入repo目录，用`git submodule update --init --recursive`，初始化子模块。

### 更新，拉取项目下已有的子模块

```bash
git submodule update --remote
```

如果，HostApp里面的submodule，信息修改了。
如改了submodule的url。则在pull了代码后，同步一下submodule信息。

```bash
git submodule sync --recursive
```

## repo 增加新的 submodule

```bash
git submodule add https://sub.sub-module1.git  module1
```

对于把本Architecture项目，引入到其它项目中，命令如下

```bash

git submodule add http://dnsdk.vimedia.cn:8080/r/~chuangxin1/AppArchNG.git modules

git submodule add http://dnsdk.vimedia.cn:8080/r/~chuangxin1/nAppMod.git ng

```

由于现在的git仓库，没有通用的`git@[host]`ssh地址。
每次`git submoudle update --remote`的时候，都要输入密码。

每个人，可以自己修改一下自己git配置，以用自己本地的url，替换上面的`http://dnsdk.vimedia.cn:8080/r/android/AppArchNG.git`

```bash
git config submodule.modules.url ssh://YourName@dnsdk.vimedia.cn:29418/r/android/AppArchNG.git

# submodule.[dirname].url, diraname这里为 'modules'
# 可以 cat .gitmodules查看
```

如果上面命令替换为`ssh`的submodule地址不好用，可以直接修改项目里面git module的config

```
cd .git\modules\modules

修改文件目录下 config 文件

修改url 为自己的ssh地址

[remote "origin"]
	url = ssh://zhangyingxiang@dnsdk.vimedia.cn:29418/android/AppArchNG.git

```

引入submodule后，原始项目就正常使用`git`，
如有submodule更新，则直接按上面的更新操作即可。

# 结构说明

默认情况下，各`模块`放在`modules`目录下。
依赖时无需要，显示指定 `:modules:`。

如一个模块叫`modx`。
其它模块引用，直接：

```gradle
implementation project(":modx")

// 无需使用 implementation project(":modules:modx")
```
## Building

在root, build.gradle里面，引入 `modules/config.gradle`,  `modules/auto_configure.gradle`(按顺序)。

- config.gradle
    全局依赖定义（group和版本）

- auto_configure.gradle
    build，多模块自动配置(依赖、开关等)

在settings.gradle里面引入 `modules/mod.gradle`。

- mod.gradle
    用于模块目录结构组织。

# 编写原则

官方指引文档 [Jetpack Guide](https://developer.android.com/jetpack/docs/guide)

## [逻辑分割](https://developer.android.com/jetpack/docs/guide#separation-of-concerns)

不要将所有（或大部分）逻辑和数据流代码，直接写在 `activity`,`fragment`,`service`中。

UI相关，引入`ViewModel`数据粘合层，通过 LiveData（或 ObservableField）对View提供可观察的数据。

模块独立后，各个组件的依赖使用 ARouter的`服务发现`(或 Dagger2)来关联。
避免直接人肉获取实例(new or getInstance)。

## UI方面

- 使用 Databinding来做布局定义。

  不一定非要使用， 数据绑定功能。
  
  **第一目的**是避免手写`findViewById`, 也不用再定义相关的 View变量。数据绑定只为其次，来配合 `MVVM`模式。

- 根据页面，逻辑复杂度，选择适合的开发范式(MVC, MVP, MVVM)

  - 交互，逻辑都简单明了，直接用`MVC`+`DatabindingLayout`即可
  - 交互数据多，逻辑相对明了。 适合 `MVVM` + `Databinding`
  - 交互一般，逻辑相对复杂。 适合`MVP`, 逻辑层 封装组合在 `Presenster`中
  - 交互复杂，逻辑也复杂的。 可使用 `MVVM`与`MVP`结合。
    
    `Presenter`直接封装组合逻辑。`ViewModel`做为胶水层，组合`Presenter`和`LiveData`。View层面，只依赖`ViewModel`,做`LiveData`数据展示。

## 脚手架

脚手架，是由一些基类和 `Kotlin`扩展函数构成。

- Activity, 统一继承于 `ViActivity`
- Fragment, 统一继承于 `ViFragment`
- Application, 统一继承于 `BaseApplication`
- Dialog, 大多数情况下，直接继承 `LightDialogBindingFragment`
- ViewPager, 配合 `PagerItemAdapter`

### 全局组件

全局组件，默认由`AppMod`来管理。
它是在`BaseApplication`中注入生成。

可使用其中的`appComponent`接口来获取各组件，默认都是全局唯一，懒加载。
（如果需要Dagger2依赖注入，可以直接依赖`appComponent`）

### Router Group

- page
  跳 Activity。 如 `/page/home`

- scene
  获取 fragment。 如 `/scene/clean/quick`

- service
  模块接口。 如 `/service/clean`


## 异步调度

异步调度主要使用`RxJava3`来控制。

对于`Kotlin`下, 有很多扩展，常用`subscribeBy{}`，可以大大简化。rxjava的操作。

`Rxjava`的生命周期自动管理，都是通过`LifecycleView`的`bindUntilDestroy`来进行。`ViActivity`和`ViFragment`都是`LifecycleView`的实现。可直接使用。

其它模块中要独立管理，可以手动实现`RxLifecycleDelegate`,并管理其生命周期，同样使用`bindUntilDestroy`来自动管理`Rxjava`

```kotlin
// 参考样例 GuidePresenter.kt

// MVP view 
// view: LoadingView, LifecycleView

RxLottieLoader(anim)
    .compose(view.bindUntilDestroy()) // 绑定生命周期, 也可直接用bindLifecycle(view)扩展
    .delay(500L, TimeUnit.MILLISECONDS)
    .observeOn(AndroidSchedulers.mainThread())
    .connectLoading(view) // 响应加载状态 kt 扩展函数
    .subscribeBy {  // 简化 subscribe调用
        view.onGuideAnimLoaded(anim, it)
    }

```

### 协程

`ViActivity`,`ViFragment`,`ViewModel`。
都有相应的 `scope`, `coroutineScope`, `viewModelScope` 函数(属性，扩展函数）来获取 CoroutineScope, 然后可以用 `launch`来启动协程。

优点： 
  - 消耗小
  - 流程控制明晰, 用同步的语法，写异步调度的功能。代码顺序就是最终执行的顺序。
  - 写法简单

缺点:
  - 不好调试 (1.3.8以后的协议可以调试了)

为了避免缺点，暂时不要对外接口暴露协程，接口还是使用`Rxjava`来封装。

```kotlin
// 上面的rx代码，协程写法

lifecycleScope.launch {

  view.startLoading()

  val loadedAnim = withContext(Dispaters.IO) {
    return@withContext syncLoadLottie(anim)
  }

  delay(500L)

  view.onGuideAnimLoaded(anim, loadedAnim)

  view.stopLoading(noError, null)

}

```


# 模块说明


```
Common
  +
  |
  +-+ Base
````

### Common

通用模块

尽量平台兼容，独立的功能组件。保证小而必须。

- 基本的依赖
- 日志
- 异步调度
- kt common extension
- http
- gson
- platform 相关工具

### Base

APP 开发相关框架与组件。

- Android 组件基类  Application/Activity/Fragment
- 数据、流控制与传递， LiveData，ObservableField, RxRelay
- Lifecycle控制与绑定组件
- UI交互开发框架范式， MVP、MVVM
- coroutine scope控制
- 图片加载，（或 自定义图片资源包加载）
- 相对复杂的 kt extension
- 自定义特定且通用的`Wdiget`和`Layout`，以及View相关`Controller`和`Helper`
- 组件解耦合 ARouter (或 Dagger2), [文档](https://github.com/alibaba/ARouter/blob/master/README_CN.md)
  服务发现。
- 列表ui组合 mulitType

### Ext

三方功能扩展包。

- Lottie rx加载器

# 样例说明

`app`目录下为样例代码。
用于演示如下功能：
  1. 初始化，服务模块定义与服务发现，路由
  2. MVVM， MVP。
  3. 依赖注入。

`1` 为各模块，组件分离的基础，**必须使用**。

`2` UI交互相关代码，推荐使用。其中用`DataBinding`去替换`findView`相关代码，是**强烈推荐**。

`3` 依赖注入，**不做要求**，因为其本身理解就很复杂。只对于复杂逻辑组件组织，有明显的优势，所在在依赖关系只有一两级的情况下，不建议使用。
但如果依赖体系过多，就有必要使用`依赖注入`做中后期重构和整理，以提高可维护性。

## 目录结构

### 初始化与服务发现

- 初始化，查看APP里面的`onCreateEnv`方法。

  注意，实际中，三方sdk，可能需要对于是否在远程进程 `remoteProc`状态，做不同的初始化处理。

- 日志

  使用`VLog`类。接口类似`Log`。

  对于`kotlin`, 打日志，可以直接使用 `debug`,`info`, `warn`, `error`扩展。并可避免不必要的字符串拼接。

  ```kotlin

    debug { "Nor: ${System.currentTimemills()}" }

    // 等同于

    if (isDebug()) {
      VLog.d("Now: " + System.currentTimemills())
    }

  ```

- 模块服务 `com.example.apparch.modules`

  定义了一个模拟服务。`CleanManager`。

  下面的`engine`，为子服务组件。

  组件模块，类都必须实现`IProvider`接口。
  并且指定`@Router(path = "/模块组/模块路径/模块名")`。
  并且它们都是全局唯一的。

  任何地方都可以直接使用`ARouter`来获取它们的唯一实例。也可以使用其注入方式，来填充组件。

  ```kotlin

  @Route(path = "/service/clean")
  class CleanManager : IProvider {

      @Autowired(name = "/clean/engine/ng1")
      lateinit var engine1: Engine

      lateinit var engine2: Engine

      override fun init(context: Context?) {
          // 注入 组件, engine1
          ARouter.getInstance().inject(this)

          // 手工获取
          // arouter()是对ARouter.getInstance()的扩展后的简写, 
          // 更多扩展查看Base里面的Navi.kt
          engine2 = arouter().build("/clean/engine/ng2").navigation() as Engine
      }
  }    

  ```

  - `MockCleanFragment`是对上面组件的应用。 
  - `MockCleanViewModel`则是`协程`+`MVVM`的样例。
  - `MainActivyt`是路由、页面跳转基本使用。

### UI范式, MVVM, MVP

#### MVVM

包`com.example.apparch.ui.lottie`下面，
是通过`MVVM` 配合 `RxJava`实现的`极速清理管家`中的Lottie动画的效果展示功能。

其中 `LottieGalleryViewModel`，以及全部依赖传递，都是使用`Dagger2`来做依赖注入演示。

`LottieGalleryFragment`演示，LiveData的UI装配。

#### MVP

包`com.example.apparch.ui.guide`。
使用`MVP`配合`RxJava`,以及`Dialog脚手架`实现一个权限引导提示流程。

`GuideActivity`和`GuidePresenter`演示`MVP`范式。实现一个引导展示效果。

`GuideAlertFragment`演示`Dialog`和`Databinding`的使用与基础配合。用很少代码，实现`Dialog`以及响应式的`Dialog`操作数据流处理。

### 依赖注入

依赖定义和组织在包`com.example.apparch.di`下。

注入点，参考`LottieGalleryFragmet.inject()`

依赖图
![依赖图](deps-overview.jpg)


