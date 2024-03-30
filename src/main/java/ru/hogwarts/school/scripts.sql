select name, age from public.student where age > 10 and age < 20;
select name, age from public.student where age between 10 and 20; // тут даже не знаю что выигрывает, по длине кода идентично
select name from public.student;
select id, name from public.student where name like '%д%';
select id, age, name from public.student where age < id;
select age, name from public.student order by age;
