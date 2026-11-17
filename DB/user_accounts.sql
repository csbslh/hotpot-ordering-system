-- 杉佳火锅用户初始账号
-- 文件说明：为 cs_hotpot 数据库新增一个管理员账号和一个普通用户账号。
-- 执行方式：在 MySQL 客户端中执行本文件，或复制其中两条 INSERT 语句执行。
-- 说明：项目当前 AuthService 使用明文密码校验，因此 password 暂时直接保存为明文。

USE cs_hotpot;

-- 管理员账号：登录页选择“管理员登录”后进入 admin.html。
INSERT INTO `user` (username, password, nickname, role, status)
VALUES ('CSBSLH', '1569', '杉佳管理员', 'ADMIN', 1)
ON DUPLICATE KEY UPDATE
    password = '1569',
    nickname = '杉佳管理员',
    role = 'ADMIN',
    status = 1;

-- 普通用户账号：登录页选择“客户登录”后进入 user.html。
INSERT INTO `user` (username, password, nickname, role, status)
VALUES ('MNSML', '27527', 'MNSML', 'USER', 1)
ON DUPLICATE KEY UPDATE
    password = '27527',
    nickname = 'MNSML',
    role = 'USER',
    status = 1;

-- 检查两个账号的角色和启用状态。
SELECT id, username, nickname, role, status
FROM `user`
WHERE username IN ('CSBSLH', 'MNSML');
