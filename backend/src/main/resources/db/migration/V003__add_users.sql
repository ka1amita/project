insert into app_users (id, active, created_at, deleted, email, password, username, verified_at)
values (1, true, now(), false, '${email}', '${password}', '${username}', now());

insert into app_users_roles (app_user_id, role_id) values (
                                                              (select id from app_users where username = 'root'),
                                                              (select id from roles where name = 'ROOT')
                                                          );
