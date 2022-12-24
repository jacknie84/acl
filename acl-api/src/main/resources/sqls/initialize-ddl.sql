insert into member_account (email, password) values ('admin@email.com', '{noop}123456');
insert into member_role (account_id, role)
values ((select id from member_account where email = 'admin@email.com'), 'ROLE_ADMIN');

insert into acl_domain (domain_code, id_type) values ('domain::member', 'java.lang.Long');
insert into acl_sid (sid_type, sid_value) values ('PRINCIPAL', 'admin@email.com');
insert into acl_object (domain_id, owner_sid, parent_id, identifier, entries_inheriting)
values (
  (select id from acl_domain where domain_code = 'domain::member'),
  (select id from acl_sid where sid_type = 'PRINCIPAL' and sid_value = 'admin@email.com'),
  (select a.id from acl_object a
    join acl_super_node b on b.id = a.identifier
    where b.node_type = 'DOMAIN'
    and b.node_code = 'domain::member'
  ),
  (select id from member_account where email = 'admin@email.com'),
  true
);
insert into acl_entry (object_id, sid, ace_order, mask, granting, audit_success, audit_failure)
values (
  (select a.id from acl_object a
    where exists (select 1 from member_account b where b.id = a.identifier and b.email = 'admin@email.com')
    and exists (select 1 from acl_domain c where c.id = a.domain_id and c.domain_code = 'domain::member')
  ),
  (select id from acl_sid where sid_type = 'PRINCIPAL' and sid_value = 'admin@email.com'),
  0,
  11,
  true, true, true
);
