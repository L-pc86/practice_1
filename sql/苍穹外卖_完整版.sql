-- 苍穹外卖完整版数据库表结构

-- 1. 员工表
CREATE TABLE IF NOT EXISTS employee (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    phone VARCHAR(11) NOT NULL COMMENT '手机号',
    sex VARCHAR(1) NOT NULL COMMENT '性别',
    id_number VARCHAR(18) NOT NULL COMMENT '身份证号',
    status INT DEFAULT 1 COMMENT '账号状态（0:禁用 1:启用）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT COMMENT '创建人',
    update_user BIGINT COMMENT '修改人'
) COMMENT '员工表';

-- 2. 分类表
CREATE TABLE IF NOT EXISTS category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    type INT COMMENT '分类类型（1:菜品分类 2:套餐分类）',
    sort INT DEFAULT 0 COMMENT '排序',
    status INT DEFAULT 1 COMMENT '状态（0:禁用 1:启用）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT COMMENT '创建人',
    update_user BIGINT COMMENT '修改人'
) COMMENT '分类表';

-- 3. 菜品表
CREATE TABLE IF NOT EXISTS dish (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(50) NOT NULL COMMENT '菜品名称',
    category_id BIGINT NOT NULL COMMENT '分类id',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    image VARCHAR(255) COMMENT '图片',
    description VARCHAR(255) COMMENT '描述',
    sort INT DEFAULT 0 COMMENT '排序',
    status INT DEFAULT 1 COMMENT '状态（0:停售 1:起售）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT COMMENT '创建人',
    update_user BIGINT COMMENT '修改人'
) COMMENT '菜品表';

-- 4. 菜品口味表
CREATE TABLE IF NOT EXISTS dish_flavor (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    dish_id BIGINT NOT NULL COMMENT '菜品id',
    name VARCHAR(50) COMMENT '口味名称',
    value VARCHAR(255) COMMENT '口味数据（JSON格式）'
) COMMENT '菜品口味表';

-- 5. 套餐表
CREATE TABLE IF NOT EXISTS setmeal (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(50) NOT NULL COMMENT '套餐名称',
    category_id BIGINT NOT NULL COMMENT '分类id',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    image VARCHAR(255) COMMENT '图片',
    description VARCHAR(255) COMMENT '描述',
    status INT DEFAULT 1 COMMENT '状态（0:停售 1:起售）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT COMMENT '创建人',
    update_user BIGINT COMMENT '修改人'
) COMMENT '套餐表';

-- 6. 套餐菜品关联表
CREATE TABLE IF NOT EXISTS setmeal_dish (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    setmeal_id BIGINT NOT NULL COMMENT '套餐id',
    dish_id BIGINT NOT NULL COMMENT '菜品id',
    name VARCHAR(50) COMMENT '菜品名称（冗余字段）',
    price DECIMAL(10,2) COMMENT '菜品价格（冗余字段）',
    copies INT NOT NULL COMMENT '份数'
) COMMENT '套餐菜品关联表';

-- 7. 订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    order_number VARCHAR(50) NOT NULL COMMENT '订单号',
    user_id BIGINT COMMENT '用户id',
    address_id BIGINT COMMENT '地址id',
    order_time DATETIME NOT NULL COMMENT '下单时间',
    checkout_time DATETIME COMMENT '结账时间',
    pay_method INT NOT NULL COMMENT '支付方式（1:微信支付 2:支付宝 3:现金）',
    amount DECIMAL(10,2) NOT NULL COMMENT '实收金额',
    remark VARCHAR(255) COMMENT '备注',
    username VARCHAR(50) COMMENT '用户名（冗余）',
    phone VARCHAR(11) COMMENT '手机号（冗余）',
    address VARCHAR(255) COMMENT '收货地址（冗余）',
    consignee VARCHAR(50) COMMENT '收货人（冗余）',
    status INT DEFAULT 1 COMMENT '订单状态（1:待付款 2:待接单 3:已接单 4:派送中 5:已完成 6:已取消）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT '订单表';

-- 8. 订单明细表
CREATE TABLE IF NOT EXISTS order_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    order_id BIGINT NOT NULL COMMENT '订单id',
    dish_id BIGINT COMMENT '菜品id',
    setmeal_id BIGINT COMMENT '套餐id',
    name VARCHAR(50) NOT NULL COMMENT '菜品名称',
    image VARCHAR(255) COMMENT '图片',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    copies INT NOT NULL COMMENT '份数'
) COMMENT '订单明细表';

-- 9. 地址簿表
CREATE TABLE IF NOT EXISTS address_book (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    user_id BIGINT NOT NULL COMMENT '用户id',
    consignee VARCHAR(50) NOT NULL COMMENT '收货人',
    phone VARCHAR(11) NOT NULL COMMENT '手机号',
    sex VARCHAR(1) NOT NULL COMMENT '性别',
    province_code VARCHAR(10) COMMENT '省code',
    province_name VARCHAR(50) COMMENT '省名称',
    city_code VARCHAR(10) COMMENT '城市code',
    city_name VARCHAR(50) COMMENT '城市名称',
    district_code VARCHAR(10) COMMENT '区code',
    district_name VARCHAR(50) COMMENT '区名称',
    detail VARCHAR(255) COMMENT '详细地址',
    label VARCHAR(50) COMMENT '标签',
    is_default INT DEFAULT 0 COMMENT '是否默认（0:否 1:是）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '地址簿表';

-- 10. 用户表（顾客）
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    phone VARCHAR(50) NOT NULL COMMENT '手机号',
    openid VARCHAR(50) COMMENT '微信openid',
    name VARCHAR(50) COMMENT '昵称',
    sex VARCHAR(1) COMMENT '性别',
    avatar VARCHAR(255) COMMENT '头像',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT '用户表';

-- 11. 购物车表
CREATE TABLE IF NOT EXISTS shopping_cart (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    user_id BIGINT NOT NULL COMMENT '用户id',
    dish_id BIGINT COMMENT '菜品id',
    setmeal_id BIGINT COMMENT '套餐id',
    dish_flavor VARCHAR(50) COMMENT '菜品口味',
    name VARCHAR(50) NOT NULL COMMENT '名称',
    image VARCHAR(255) COMMENT '图片',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    number INT NOT NULL DEFAULT 1 COMMENT '数量',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT '购物车表';

-- 12. 店铺状态表
CREATE TABLE IF NOT EXISTS shop (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    status INT DEFAULT 0 COMMENT '营业状态（0:打烊 1:营业中）',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '店铺状态表';

-- 初始化店铺状态
INSERT INTO shop (id, status) VALUES (1, 1);

-- 初始化数据
-- 插入管理员账号 (密码: 123456, MD5加密)
INSERT INTO employee (name, username, password, phone, sex, id_number, status) VALUES
('管理员', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '13800138000', '1', '110101199001011234', 1);

-- 插入测试用户
INSERT INTO user (phone, name, sex) VALUES
('13800138001', '测试用户', '1');
