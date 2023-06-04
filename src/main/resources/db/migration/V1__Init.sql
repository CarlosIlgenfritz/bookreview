CREATE TABLE IF NOT EXISTS public.book_review (
   	book_id int4 NULL,
   	rating int4 NULL,
   	review_date date NULL,
   	id uuid NOT NULL,
   	review varchar(255) NULL
   );