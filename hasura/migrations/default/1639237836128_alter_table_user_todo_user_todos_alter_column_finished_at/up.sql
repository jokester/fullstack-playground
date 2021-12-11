ALTER TABLE "user_todo"."user_todos" ALTER COLUMN "finished_at" TYPE timestamp with time zone;
alter table "user_todo"."user_todos" alter column "finished_at" drop not null;
