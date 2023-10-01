alter table `app_users`
    add column `lang` varchar(2) not null default 'en';
alter table `app_users`
    alter column `lang` drop default;