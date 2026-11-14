AI COUNCIL PWA v1.0

ไฟล์:
- index.html: หน้าแอป
- manifest.webmanifest: ตั้งค่าแอปติดตั้งบนหน้าจอ
- sw.js: cache สำหรับการเปิดซ้ำ

ข้อจำกัด:
- ไม่ใช้ API และไม่เชื่อมบัญชี ChatGPT/Gemini โดยตรง
- ผู้ใช้ต้องเปิดแต่ละบริการและคัดลอก/วางคำตอบเอง
- ข้อมูลเก็บใน localStorage ของเบราว์เซอร์บนอุปกรณ์

ติดตั้งเป็นแอป:
1. อัปโหลดไฟล์ทั้งหมดไป GitHub repository
2. เปิด Settings > Pages
3. เลือก Deploy from a branch > main > /(root) แล้ว Save
4. รอ URL Pages จาก GitHub
5. เปิด URL ด้วย Chrome บน Android
6. เมนู Chrome (⋮) > Add to Home screen หรือ Install app

ต้องเผยแพร่ผ่าน HTTPS ก่อนจึงจะติดตั้ง PWA และใช้ service worker ได้
