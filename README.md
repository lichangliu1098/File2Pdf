# 项目介绍：利用aspose工具包实现word，ppt转pdf功能 File2Pdf

aspose相关jar包太大，有多个版本，不好上传到git，单独放到一个下载链接，下载后放到libs文件夹下即可
https://download.csdn.net/download/qq_23169881/86265854

font下面的是字体，里面的文件包含微软雅黑和宋体，如果需要其他字体放进去即可，mac可在自带的软件[字体册]里直接下载，windows的字体在目录C:\Windows\Fonts下

Dockerfile内容是简单构建的一个容器，方便本地测试，如果你的项目环境是容器化的，建议一定要在本地容器测试下功能

为方便测试，在src同一层级目录下新建libs，ppt文件夹放入对应内容，pdf文件夹为生成的目录