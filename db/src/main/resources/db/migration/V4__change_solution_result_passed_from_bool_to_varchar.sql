alter table solution_results add column status varchar(255) not null;

update solution_results set status = 'PENDING' where 1=1;
update solution_results set status = 'PASSED_CORRECT' where passed = 1;
update solution_results set status = 'PASSED_INCORRECT' where passed = 0;

alter table solution_results drop column passed;
