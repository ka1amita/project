insert into app_users (active, created_at, deleted, email, password, username, verified_at)
values (true, now(), false, '${root.email}', '${root.password}', '${root.username}', now());

insert into app_users_roles (app_user_id, role_id) values (
                                                              (select id from app_users where username = '${root.username}'),
                                                              (select id from roles where name = '${root.role}')
                                                          );
