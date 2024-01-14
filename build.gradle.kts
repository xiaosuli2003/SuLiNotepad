// 顶级构建文件，您可以在其中添加所有子项目/模块通用的配置选项。
@Suppress("DSL_SCOPE_VIOLATION") // TODO: 当KTIJ-19369固定后移除
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.ksp) apply false
}
true  // 需要使Suppress注解为插件块工作