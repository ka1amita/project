insert into app_users (active, created_at, deleted, email, password, username, verified_at)
values (true, now(), false, 'user@gfa.com', 'password', 'user', now());

insert into app_users_roles (app_user_id, role_id) values (
                                                              (select id from app_users where username = 'user'),
                                                              (select id from roles where name = 'USER')
                                                          );
