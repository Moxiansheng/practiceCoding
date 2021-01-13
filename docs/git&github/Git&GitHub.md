[TOC]

# Git&GitHub

## [Git](https://git-scm.com)

### 1. Git优势

- 大部分操作在本地完成，不需要联网
- 完整性保证
- 尽可能添加数据而不是删除或修改数据
- 分支操作非常快捷流畅
- 与Linux命令全面兼容

### 2. Git结构

工作区（写代码）---git add--->暂存区（临时存储）---git commit--->本地库（历史版本）

### 3. Git命令行操作

#### 3.1 本地库初始化

```bash
git init
```

#### 3.2 设置签名

- 项目级别/仓库级别
  - 仅当前本地库有效

  - ```bash
    git config user.name [github name]
    git config user.email [github email]
    ```

  - 存于：./.git/config

- 系统用户级别

  - 当前登录操作系统的用户有效

  - ```bash
    git config -global user.name [github name]
    git config -global user.email [github email]
    ```

  - 存于：~/.gitconfig

- 就近原则选择签名，项目级>系统用户级

#### 3.3 基本操作

##### 3.3.1 状态查看

```bash
git status
```

##### 3.3.2 添加

```bash
git add [file name]
```

##### 3.3.3 提交

```bash
git commit -m "commit message" [file name]
```

##### 3.3.4 查看历史纪录

```bash
git log #完整信息
git log --pretty=oneline #完整索引
git log --oneline #局部索引
git reflog #局部索引 HEAD指针
```

##### 3.3.5 前进后退

- 基于索引值操作：可进可退

  ```bash
  git reset --hard [part index]
  ```

- ^：只退不进

  ```bash
  git reset --hard HEAD^ #一个^后退一步，n即n步
  ```

- ~：只退不进

  ```bash
  git reset --hard HEAD~n #后退n步
  ```

##### 3.3.6 reset参数比较

| 参数    | 本地库   | 暂存区    | 工作区    |
| ------- | -------- | --------- | --------- |
| --soft  | affected | no affect | no affect |
| --mixed | affected | affected  | no affect |
| --hard  | affected | affected  | aafected  |

##### 3.3.7 删除文件

- 前提：删除前，即文件存在时已被提交至本地库

  ```bash
  git reset --hard [pointer loc]
  ```

##### 3.3.8 比较文件差异

```bash
git diff [file name] #将工作区中的文件和暂存区进行比较
git diif [history version][file name] #将工作区中的文件和历史记录比较
git diff #比较多个文件
```

#### 3.4 分支管理

##### 3.4.1 分支好处

- 同时并行推进多个功能开发，提高开发效率
- 各个分支在开发过程中，如果某一个分支开发失败，不对其它分支产生影响

##### 3.4.2 分支操作

- 创建分支

```bash
git branch [branch name]
```

- 查看分支

```bash
git branch -v
```

- 切换分支

```bash
git checkout [branch name]
```

- 合并分支

  - first step:切换到接受修改的分支

  ```bash
  git checkout [接受合并分支名]
  ```

  - second step:执行合并命令

  ```bash
  git merge [被接收分支名]
  ```

##### 3.4.3 冲突解决

1. 编辑文件，删除特殊符号

2. 把文件修改到满意的程度，保存退出

3. ```bash
   git add [file name]
   ```

4. ```bash
   git commit -m "commit message" #不能带文件名
   ```

------

## [GitHub](https://github.com)

### 本地Git连接远程GitHub

#### 1. GitHub远程仓库图形化创建

#### 2. 命令行操作

##### 2.1 远程仓库地址别名

```bash
git remote -v #查看
git remote add [alias] [remote address]
```

##### 2.2 拉取远程

```bash
git pull origin master
#若遇到 fatal: refusing to merge unrelated histories
git pull origin maste --allow-unrelated-histories 
#或分fetch与merge两步进行操作
pull = fetch + merge
```

##### 2.3 推送远程

```bash
git push origin master
#若推送出错，可先进行拉取，或执行下列操作
git push --force origin master #强制推送

```

##### 2.4 克隆项目

```bash
git clone [https/ssh]
```

#### 3. ssh免密登录

##### 3.1 进入当前用户的根目录

```bash
cd ~
```

##### 3.2 若存在.ssh跳至3.3

```bash
ssh-keygen -t rsa -C [github email]
```

##### 3.3 进入.shh目录查看文件列表

```bash
cd .ssh
ls -lf
```

##### 3.4 查看id_rsa.pub

```bash
cat id_rsa.pub
```

##### 3.5 复制内容至GitHub，点击用户->Settings->SSH and GPG keys->New SSH Key

##### 3.6 输入复制的密钥信息

##### 3.7 创建远程地址别名

```bash
git remote add origin [ssh]
```

