create table solution_results (
  id int not null,
  output text,
  points int default 0,
  passed int not null default 0,
  test_case_id int not null,
  solution_id int not null,
  primary key (id)
);
