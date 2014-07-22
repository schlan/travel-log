# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "DAY_TOUR" ("DATE" DATE NOT NULL,"DAYTOUR_ID" OTHER NOT NULL PRIMARY KEY,"DAYTOUR_START" BIGINT NOT NULL,"DAYTOUR_END" BIGINT NOT NULL,"DESCRIPTION" VARCHAR NOT NULL,"WEATHER_COND" VARCHAR NOT NULL,"ROAD_COND" VARCHAR NOT NULL);
create unique index "INDEX_DATE" on "DAY_TOUR" ("DATE");
create table "IMAGES" ("NAME" VARCHAR NOT NULL,"DATETIME" TIMESTAMP NOT NULL,"DATETIME_ZONE" VARCHAR NOT NULL,"PATH" VARCHAR NOT NULL);
create table "TRACK" ("TRACK_ID" OTHER NOT NULL PRIMARY KEY,"TRACK_NAME" VARCHAR,"TRACK_ACTIVITY_TYPE" VARCHAR NOT NULL);
create table "TRACK_METADATA" ("TRACK_ID" OTHER NOT NULL PRIMARY KEY,"DESCRIPTION" VARCHAR,"DISTANCE" FLOAT,"TIMERTIME" FLOAT,"TOTALELAPSEDTIME" FLOAT,"MOVINGTIME" FLOAT,"STOPPEDTIME" FLOAT,"MOVINGSPEED" FLOAT,"MAXSPEED" FLOAT,"MAXELEVATION" FLOAT,"MINELEVATION" FLOAT,"ASCENT" FLOAT,"DESCENT" FLOAT,"AVGASCENTRATE" FLOAT,"MAXASCENTRATE" FLOAT,"AVGDESCENTRATE" FLOAT,"MAXDESCENTRATE" FLOAT,"CALORIES" FLOAT,"AVGHEARTREATE" FLOAT,"AVGCADENCE" FLOAT,"DISPLAYCOLOR" INTEGER);
create table "TRACK_POINTS" ("TRACK_ID" OTHER NOT NULL,"LATITUDE" FLOAT NOT NULL,"LONGITUDE" FLOAT NOT NULL,"ELEVATION" FLOAT NOT NULL,"DATE" TIMESTAMP NOT NULL PRIMARY KEY,"TIME_ZONE" VARCHAR NOT NULL);
create unique index "TRACK_POINT_IDX" on "TRACK_POINTS" ("DATE");
create table "TRIP" ("TITLE" VARCHAR NOT NULL,"DESC" VARCHAR DEFAULT 'No Description' NOT NULL,"SHORT_NAME" VARCHAR NOT NULL PRIMARY KEY,"START_DATE" DATE NOT NULL,"END_DATE" DATE NOT NULL);
create table "USER" ("UNAME" VARCHAR NOT NULL PRIMARY KEY,"PASSWORD" VARCHAR NOT NULL,"DNAME" VARCHAR NOT NULL);
alter table "TRACK_METADATA" add constraint "TRACK_FK" foreign key("TRACK_ID") references "TRACK"("TRACK_ID") on update NO ACTION on delete NO ACTION;
alter table "TRACK_POINTS" add constraint "TRACK_POINT_FK" foreign key("TRACK_ID") references "TRACK"("TRACK_ID") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "TRACK_METADATA" drop constraint "TRACK_FK";
alter table "TRACK_POINTS" drop constraint "TRACK_POINT_FK";
drop table "DAY_TOUR";
drop table "IMAGES";
drop table "TRACK";
drop table "TRACK_METADATA";
drop table "TRACK_POINTS";
drop table "TRIP";
drop table "USER";

