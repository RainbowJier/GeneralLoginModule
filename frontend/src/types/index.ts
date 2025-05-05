interface PersonInter{
   id:string,
   name:string,
   age:number,
}

type persons = Array<PersonInter>;

export type { PersonInter,persons };