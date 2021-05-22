CREATE TABLE "todoapp_users" (
    "user_id" BIGSERIAL NOT NULL PRIMARY KEY,
    "user_email" TEXT NOT NULL UNIQUE,
    "user_profile" JSONB NOT NULL,
    "password_hash" TEXT NOT NULL,
    "created_at" TIMESTAMP WITH TIME ZONE NOT NULL,
    "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE "todoapp_todos" (
    "todo_id" BIGSERIAL NOT NULL PRIMARY KEY,
    "user_id" INTEGER NOT NULL,
    "title" TEXT NOT NULL,
    "description" TEXT NOT NULL,
    "finished_at" TIMESTAMP WITH TIME ZONE NULL,
    "created_at" TIMESTAMP WITH TIME ZONE NOT NULL,
    "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY ("user_id") REFERENCES "todoapp_users"("user_id")
);
