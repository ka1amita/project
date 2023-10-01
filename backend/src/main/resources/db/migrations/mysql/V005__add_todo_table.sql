create table `todos`
(
    `id`          bigint       not null auto_increment,
    `completed`   bit          not null,
    `deleted`     bit          not null,
    `description` varchar(255) not null,
    `due_date`    TIMESTAMP,
    `title`       varchar(255) not null,
    `app_user_id` bigint,
    primary key (`id`)
)
    engine = InnoDB;
alter table `todos`
    add constraint FKml5lb81vmi2r8kiby3jhni9bn foreign key (`app_user_id`) references `app_users` (`id`);