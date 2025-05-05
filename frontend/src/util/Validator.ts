export class Validator {
   /**
    * 校验是否为合法手机号（以中国手机号为例）
    */
   static isPhone(phone: string): boolean {
     const phoneRegex = /^1[3-9]\d{9}$/;
     return phoneRegex.test(phone);
   }
 
   /**
    * 校验是否为合法邮箱
    */
   static isEmail(email: string): boolean {
     const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
     return emailRegex.test(email);
   }

   static isNumber(str: string): boolean {
      return !isNaN(Number(str)) && str.trim() !== '';
    }

 }