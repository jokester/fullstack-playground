alter table "user_todo"."user_todos" alter column "finished_at" set not null;
ALTER TABLE "user_todo"."user_todos" ALTER COLUMN "finished_at" TYPE timestamp with time zone;
