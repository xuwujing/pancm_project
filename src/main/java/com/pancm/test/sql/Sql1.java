package com.pancm.test.sql;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:


-- 部门表的组织查询的示例
-- 部门表

CREATE TABLE `department` (
`id` int(11) NOT NULL,
`parent_id` int(11) DEFAULT NULL,
`name` varchar(50) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO department (id, parent_id, NAME) VALUES
(1, NULL, '总公司'),
(2, 1, '人事部'),
(3, 1, '财务部'),
(4, 1, '市场部'),
(5, 2, '招聘组'),
(6, 2, '培训组'),
(7, 5, '招聘一部'),
(8, 5, '招聘二部'),
(9, 6, '培训一部'),
(10, 6, '培训二部'),
(11, 4, '推广组'),
(12, 11, '线上推广部'),
(13, 11, '线下推广部'),
(14, 12, 'SEM'),
(15, 12, 'SEO');

-- 自上而下查询 所有部门
SELECT au.id, au.name, au.parent_id
FROM (SELECT * FROM department WHERE parent_id IS NOT NULL) au,
(SELECT @pid := '2') pd
WHERE FIND_IN_SET(parent_id, @pid) > 0
AND @pid := CONCAT(@pid, ',', id)
UNION
SELECT id, NAME, parent_id
FROM department
WHERE id = '2'
ORDER BY id;

-- 多个⽗节点查询所有⼦节点（包含）

SELECT au.id, au.name, au.pid
FROM (SELECT * FROM dept WHERE pid IS NOT NULL) au,
(SELECT @pid := '1002,1005') pd
WHERE FIND_IN_SET(pid, @pid) > 0 and @pid := concat(@pid, ',', id)
UNION
SELECT id, name, pid
FROM dept
WHERE FIND_IN_SET(id, @pid) > 0
ORDER BY id;


-- 根据一个子节点id，查询所有父节点（包含⾃⾝）
SELECT t2.id, t2.name, t2.pid
FROM (SELECT @r as _id,
(SELECT @r := pid FROM dept WHERE id = _id) as pid,
 @l := @l + 1 as lvl
 FROM (SELECT @r := '1014', @l := 0) vars, dept as h
 WHERE @r <> 0) t1
 JOIN dept t2
 ON t1._id = t2.id
 ORDER BY T1.lvl DESC;


 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2023/5/6
 */
public class Sql1 {


}
