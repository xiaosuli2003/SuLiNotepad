# SuLiNotepad（酥梨日记本）

一个简洁、优雅的Android记事本应用，仿照小米笔记设计，提供流畅的用户体验和丰富的功能。

## 📱 功能特性

- ✅ **基本笔记功能**：创建、编辑、删除笔记
- ✅ **搜索功能**：支持按标题和内容搜索笔记
- ✅ **双模式切换**：支持列表视图和网格视图切换
- ✅ **深色模式**：自动适配系统深色/浅色模式
- ✅ **数据持久化**：使用Room数据库存储笔记数据
- ✅ **下拉刷新**：支持下拉刷新笔记列表
- ✅ **优雅的UI设计**：现代化Material Design风格界面

## 🛠️ 技术栈

- **开发语言**：Kotlin
- **开发框架**：Android SDK
- **UI组件**：Material Components
- **数据库**：Room
- **架构组件**：ViewModel、LiveData
- **其他**：ViewBinding、RecyclerView、SwipeRefreshLayout

## 📦 安装与运行

### 环境要求

- Android Studio 2022.3.1 或更高版本
- Android SDK 34（targetSdk）
- Android SDK 24 或更高版本（minSdk）
- Kotlin 1.8.0 或更高版本

### 安装步骤

1. 克隆项目到本地
   ```bash
   git clone https://github.com/xiaosuli2003/SuLiNotepad.git
   ```

2. 使用Android Studio打开项目

3. 等待Gradle同步完成

4. 连接Android设备或启动模拟器

5. 点击Run按钮运行应用

## 📖 使用说明

### 创建笔记

1. 点击右下角的「+」按钮
2. 输入标题和内容
3. 点击右上角的「保存」按钮，或直接返回自动保存

### 编辑笔记

1. 点击任意笔记卡片进入编辑页面
2. 修改标题或内容
3. 点击右上角的「保存」按钮，或直接返回自动保存

### 删除笔记

1. 长按任意笔记卡片
2. 在弹出的确认对话框中点击「确定」

### 搜索笔记

1. 在顶部搜索栏输入关键词
2. 点击搜索图标或键盘上的搜索按钮
3. 查看搜索结果

### 切换视图模式

1. 点击右上角的菜单按钮
2. 选择列表视图或网格视图

### 刷新笔记

- 向下拉动笔记列表进行刷新

## 📁 项目结构

```
SuLiNotepad/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/xiaosuli/notepad/
│   │   │   │   ├── Note.kt               # 笔记实体类
│   │   │   │   ├── NoteDao.kt            # 数据库操作接口
│   │   │   │   ├── NoteRepository.kt     # 数据仓库
│   │   │   │   ├── AppDataBase.kt        # Room数据库类
│   │   │   │   ├── NoteViewModel.kt      # 笔记ViewModel
│   │   │   │   ├── MainActivity.kt       # 主页面
│   │   │   │   ├── AddActivity.kt        # 添加笔记页面
│   │   │   │   ├── EditActivity.kt       # 编辑笔记页面
│   │   │   │   └── NoteAdapter.kt        # 笔记列表适配器
│   │   │   ├── res/
│   │   │   │   ├── layout/                   # 布局文件
│   │   │   │   ├── drawable/                 # 图片资源
│   │   │   │   ├── values/                   # 字符串、颜色等资源
│   │   │   │   └── values-night/             # 深色模式资源
│   │   │   └── AndroidManifest.xml           # 应用配置文件
│   └── build.gradle.kts                      # 模块构建配置
├── build.gradle.kts                          # 项目构建配置
├── settings.gradle.kts                       # 项目设置
└── README.md                                 # 项目说明文档
```

## 🎨 界面预览

### 主界面

- 网格视图
- 列表视图
- 搜索功能

### 编辑界面

- 简洁的编辑界面
- 自动保存功能

## 🔧 核心功能实现

### 数据库设计

使用Room数据库实现数据持久化，定义了Note实体类和NoteDao接口：

```kotlin
@Entity
data class Note(var title: String, var content: String, var time: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

@Dao
interface NoteDao {
    @Insert
    fun insertNote(note: Note): Long

    @Query("delete from Note where id = :id")
    fun deleteNote(id: Int)

    @Update
    fun updateNote(vararg newNote: Note)

    @Query("select * from Note")
    fun queryAllNote(): LiveData<List<Note>>

    @Query("select * from Note where title like '%' || :keyword || '%' or content like '%' || :keyword || '%'")
    fun queryNoteByKeyword(keyword: String): List<Note>
}
```

### 架构设计

采用MVVM架构模式：

- **Model**：负责数据的存储和获取（Room数据库）
- **View**：负责UI的展示和用户交互（Activity、Fragment）
- **ViewModel**：负责连接Model和View，处理业务逻辑

### 深色模式适配

通过values和values-night目录下的资源文件实现深色模式适配：

```xml
<!-- values/themes.xml -->
<style name="Theme.NotePad2" parent="Theme.MaterialComponents.DayNight.NoActionBar">
    <item name="colorPrimary">@color/yellow</item>
    <item name="android:statusBarColor">@color/white</item>
</style>

    <!-- values-night/themes.xml -->
<style name="Theme.NotePad2" parent="Theme.MaterialComponents.DayNight.NoActionBar">
<item name="colorPrimary">@color/yellow</item>
<item name="android:statusBarColor">?attr/colorPrimaryVariant</item>
<item name="android:background">@color/black</item>
<item name="android:textColor">@color/white</item>
</style>
```

## 📄 许可证

本项目采用Apache License 2.0许可证。详见[LICENSE](LICENSE)文件。

## 📧 联系方式

如有问题或建议，请通过以下方式联系：

- Email: xiaosuli2003@qq.com
- GitHub
  Issues: [https://github.com/xiaosuli2003/SuLiNotepad/issues](https://github.com/xiaosuli2003/SuLiNotepad/issues)

## 🙏 致谢

- 感谢小米笔记提供的设计灵感
- 感谢Android Jetpack团队提供的优秀组件
- 感谢所有开源项目的贡献者

---

**如果本项目对你有帮助，请给个Star ⭐ 支持一下！**