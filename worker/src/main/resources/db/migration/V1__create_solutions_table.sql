create table solutions (
  id int not null,
  author varchar(255) not null,
  module_name varchar(255) not null,
  source text not null,
  error_message text,
  language varchar(255) not null,
  points int default 0,
  status varchar(100) not null,
  created_at time not null,
  primary key (id)
);
