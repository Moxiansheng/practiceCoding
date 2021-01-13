# [Maven](http://maven.apache.org/)

## 1. What is Maven?

### 1.1 Maven简介

​		Maven是Apache软件基金会组织维护的一款**自动化构建**工具，专注服务于 Java平台的**项目构建**和**依赖管理**。

### 1.2 什么是构建

​		构建就是以我们编写的Java代码、框架配置文件、国际化等其他资源文件、JSP页面和图片等静态资源作为原材料去生产出一个可以运行的项目的过程。

### 1.3 构建的主要环节

1. 清理：删除之前的编译结果，为重新编译做好准备
2. 编译：将Java源程序编译为字节码文件
3. 测试：针对项目中的关键点进行测试，确保项目在迭代开发过程中的正确性
4. 报告：在每一次测试后以标准的格式记录和展示测试结果。
5. 打包：将一个包含诸多文件的工程封装为一个压缩文件用于安装或部署。 Java工程对应jar包，Web工程对应war包。
6. 安装：在Maven环境下特指将打包的结果jar包或war包安装到本地仓库中。
7. 部署：将打包的结果部署到远程仓库或将war包部署到服务器上运行。

### 1.4 如何实现

​	Maven的核心程序中仅仅定义了抽象的生命周期，而具体的操作则是由Maven的插件来完成的。可是Maven 的插件并不包含在Maven的核心程序中在首次使用时需要联网下载。下载得到的插件会被保存到本地仓库中。本地仓库默认的位置是：~~\\.m2 repository 

## 2. Maven核心概念

### 2.1 POM

​	Project Object Model：项目对象模型。将Java工程的相关信息封装为对象作为便于操作和管理的 模型。Maven工程的核心配置。可以说学习Maven就是学习pom.xml文件中的配置。

### 2.2 约定的目录结构

——Project（根目录）

————.settings

————src（源码目录）

——————main（主程序目录）

————————java（主程序的Java源文件目录）

————————resources（主程序的资源文件目录）

——————test（测试程序目录）

————————java（测试程序的Java源文件目录）

————————resources（测试程序的资源文件目录）

————target（编译结果）

### 2.3 坐标

#### 2.3.1 定义

使用如下三个向量在Maven的仓库中唯一的确定一个Maven工程。
[1]groupid：公司或组织的域名倒序 当前项目名称
[2]artifactId：当前项目的模块名称
[3]version：当前模块的版本

```xml
<dependency>
    <groupId>com.mo.practiceCoding</groupId>
    <artifactId>mavenJava</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

#### 2.3.2 使用

将三个向量拼接为地址

com/mo/practiceCoding/mavenJava/1.0-SNAPSHOT/mavenJava-1.0-SNAPSHOT.jar

### 2.4 依赖管理

#### 2.4.1 依赖添加

```xml
<dependencies>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.8.1</version>
        </dependency>
</dependencies>
```

#### 2.4.2 依赖范围

```xml
<scope>complie</scope>
```

scope设置内可选值很多，仅对compile，test，provided三个进行比较

| 影响范围 | complie | test | provided |
| -------- | ------- | ---- | -------- |
| 主程序   | yes     | no   | yes      |
| 测试程序 | yes     | yes  | yes      |
| 参与部署 | yes     | no   | no       |

#### 2.4.3 依赖传递

complie可以进行传递，test与provided不行

#### 2.4.4 依赖排除

```xml
<exclusions>
	<exclusion>
		<groupId>commons logging</groupId>
		<artifactId>commons logging</artifactId>
	</exclusion>
</exclusions>
```

#### 2.4.5 统一管理依赖jar包的版本

- 统一声明版本号

```xml
<properties>
	<mo.spring.version>4.1.1.RELEASE</mo.spring.version>
</properties>
```

- 引用声明过的版本号

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>${mo.spring.version}</version>
        </dependency>
</dependencies>
```

- 其他用法

```xml
<properties>
	<project.build.soureEncoding>UTF-8</project.build.soureEncoding>
</properties>
```

#### 2.4.6 依赖原则

1. 路径最短者优先
2. 路径相同时先声明者有限（先声明指dependency标签顺序）

### 2.5 仓库管理

#### 2.5.1 仓库分类

- 本地仓库：为当前本机电脑上的所有Maven工程服务。
- 远程仓库：私服；中央仓库；中央仓库镜像

#### 2.5.2 仓库内容

- Maven插件
- 第三方框架或工具jar包
- 自己的jar包

### 2.6 生命周期

#### 2.6.1 什么是Maven生命周期

Maven生命周期定义了各构建环节执行顺序，有此顺序，Maven就可以自动化的执行构建命令了。

#### 2.6.2 三套相互独立的生命周期

- Clean Lifecycle：在进行真正的构建之前进行一些清理工作。
- Default Lifecycle：构建的核心部分，编译，测试，打包，安装，部署等等。
- Site Lifecycle：生成项目报告，站点，发布站点。

#### 2.6.3 生命周期与自动化构建

运行任何一个阶段的时候，它前面的所有阶段都会被运行。

### 2.7 插件和目标

- Maven 的核心仅仅定义了抽象的生命周期，具体的任务都是交由插件完成的。
- 每个插件都能实现多个功能，每个功能就是一个插件目标。
- Maven的生命周期与插件目标相互绑定，以完成某个具体的构建任务。

### 2.8 继承

#### 2.8.1 创建父工程

```xml
<!-- 注意要以pom方式打包 -->
<packaging>pom</packaging> 
<!-- 将Parent项目中的dependencies标签，用dependencyManagement标签括起来 -->
```
```xml
<dependencyManagement>
	<dependencies>
		<dependency>
        	<groupId>...</groupId>
    		<artifactId>...</artifactId>
    		<version>...</version>
            <scope>...</scope>
		</dependency>
	</dependencies>
</dependencyManagement>
```

#### 2.8.2 在子工程中引用父工程

```xml
<!-- 若子工程的groupID，version与父工程重复可删除 -->
<parent>
    <groupId>...</groupId>
    <artifactId>...</artifactId>
    <version>...</version>
    <relativePath>[当前目录到父项目pom.xml的相对路径]</relativePath>
</parent>
```
```xml
<!-- 在子项目中重新指定需要的依赖，删除范围和版本号 -->
<dependencies>
	<dependency>
		<groupId>junit</groupId>
		<artifactId>junit</artifactId>
	</dependency>
</dependencies>
```

### 2.9 聚合

#### 2.9.1 为什么聚合

将多个工程拆分为模块后，需要手动逐个安装到仓库后依赖才能够生效。修改源码后也需要逐个手动进行clean操作。而使用了聚合之后就可以批量进行Maven工程的安装、清理工作。

#### 2.9.2 怎么聚合

```xml
<modules>
	<module>../Hello</module>
	<module>../HelloFriend</module>
	<module>../MakeFriends</module>
</modules>
```



## [Maven Repository](https://mvnrepository.com/)