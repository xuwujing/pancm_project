## 数据库地址
192.168.9.52:3306/mms


本项目由于连接oracle数据库，额外添加ojdbc6-11.2.0.3.jar文件，可直接添加至项目依赖或采用maven导入
导入语法如下：
mvn install:install-file -Dfile=jar包的位置(参数一) -DgroupId=groupId(参数二) -DartifactId=artifactId(参数三) -Dversion=version(参数四) -Dpackaging=jar
如：
mvn install:install-file -Dfile="D:\Program Files\mvn\ojdbc6-10.2.0.3.jar" -DgroupId=com.oracle -DartifactId=com.oracle -Dversion=10.2.0.3 -Dpackaging=jar
或导入依赖修改为以下格式:
<dependency>
<groupId>com.oracle.database.jdbc</groupId>
<artifactId>ojdbc6</artifactId>
<version>11.2.0.4</version>
</dependency>

