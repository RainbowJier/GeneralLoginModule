// 获取登录token
export function getToken():string {
  let token = localStorage.getItem("Authorization");
  return token != undefined ? token : "";
}

// 生成随机字符串
export function generateRandomString(length: number = 10): string {
  const characters =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  let result = "";
  for (let i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * characters.length));
  }
  return result;
}

// 格式化日期
export function formatDate(date: string | Date,format: string = "YYYY-MM-DD"): string {
  const d = new Date(date);
  const year = d.getFullYear();
  const month = (d.getMonth() + 1).toString().padStart(2, "0");
  const day = d.getDate().toString().padStart(2, "0");

  if (format === "YYYY-MM-DD") {
    return `${year}-${month}-${day}`;
  } else if (format === "MM/DD/YYYY") {
    return `${month}/${day}/${year}`;
  }
  return "";
}





