create table if not exists roles
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine = InnoDB
  DEFAULT CHARSET = UTF8MB4;
alter table roles
    add constraint UK_ofx66keruapi6vyqpv6f2or37 unique (name);

create table if not exists app_users
(
    id          bigint       not null auto_increment,
    active      bit          not null,
    created_at  TIMESTAMP    not null,
    deleted     bit          not null,
    email       varchar(255) not null,
    password    varchar(255) not null,
    username    varchar(255) not null,
    verified_at TIMESTAMP,
    primary key (id)
) engine = InnoDB
  DEFAULT CHARSET = UTF8MB4;
alter table app_users
    add constraint UK_spsnwr241e9k9c8p5xl4k45ih unique (username);
alter table app_users
    add constraint UK_4vj92ux8a2eehds1mdvmks473 unique (email);

create table if not exists activation_codes
(
    id              bigint       not null auto_increment,
    activation_code varchar(255) not null,
    created_at      datetime(6)  not null,
    app_user_id     bigint,
    primary key (id)
) engine = InnoDB
  DEFAULT CHARSET = UTF8MB4;
alter table activation_codes
    add constraint UK_8pjngo69fa03naejodctkpo54 unique (activation_code);
alter table activation_codes
    add constraint FKgk34atc0al1xlfask3is4r6kc foreign key (app_user_id) references app_users (id);

create table if not exists app_users_roles
(
    app_user_id bigint not null,
    role_id     bigint not null,
    primary key (app_user_id, role_id)
) engine = InnoDB
  DEFAULT CHARSET = UTF8MB4;
alter table app_users_roles
    add constraint FK39u46wfuk650kxgrw2eaayqp foreign key (role_id) references roles (id);
alter table app_users_roles
    add constraint FKjbe8lt8c1um2wc23xniiakyuf foreign key (app_user_id) references app_users (id);
