package com.aicouncil.mobile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF0B1220)
private val Panel = Color(0xFF172338)
private val Blue = Color(0xFF60A5FA)
private val Muted = Color(0xFF9CAFC5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("council", MODE_PRIVATE)
        setContent {
            var topic by remember { mutableStateOf("") }
            var gpt by remember { mutableStateOf("") }
            var gemini by remember { mutableStateOf("") }
            var summary by remember { mutableStateOf("") }
            var tab by remember { mutableStateOf("home") }
            val clipboard = LocalClipboardManager.current

            MaterialTheme(colorScheme = darkColorScheme(
                primary = Blue, background = Navy, surface = Panel,
                onBackground = Color.White, onSurface = Color.White
            )) {
                Surface(Modifier.fillMaxSize(), color = Navy) {
                    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFF1E3A5F)) {
                                Text("✦", Modifier.padding(14.dp), color = Blue, fontSize = 28.sp)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("AI COUNCIL", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                Text("GPT × Gemini · Native Android", color = Muted, fontSize = 12.sp)
                            }
                        }
                        Spacer(Modifier.height(22.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("home" to "ประชุม", "history" to "ประวัติ").forEach { (key, label) ->
                                FilterChip(selected = tab == key, onClick = { tab = key }, label = { Text(label) })
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        if (tab == "home") {
                            Text("เริ่มการประชุมใหม่", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text("พิมพ์โจทย์ที่ต้องการให้ GPT วิเคราะห์ แล้วส่งต่อให้ Gemini ตรวจสอบ", color = Muted)
                            Spacer(Modifier.height(12.dp))
                            OutlinedTextField(value = topic, onValueChange = { topic = it },
                                modifier = Modifier.fillMaxWidth(), minLines = 3,
                                label = { Text("หัวข้อ / โจทย์ประชุม") })
                            Spacer(Modifier.height(14.dp))
                            CouncilCard("1", "GPT · ผู้เสนอ", "วิเคราะห์โจทย์และเสนอแนวทาง") {
                                val prompt = "คุณคือ GPT ใน AI COUNCIL กรุณาวิเคราะห์โจทย์นี้อย่างเป็นระบบ ระบุสมมติฐาน ข้อดี ข้อเสีย ความเสี่ยง และข้อเสนอแนะ:\n\n$topic"
                                ActionButton("คัดลอกคำสั่งให้ GPT") { clipboard.setText(AnnotatedString(prompt)) }
                                ActionButton("เปิด ChatGPT") { openUrl("https://chatgpt.com") }
                                OutlinedTextField(gpt, { gpt = it }, Modifier.fillMaxWidth(), minLines = 3, label = { Text("วางคำตอบจาก GPT") })
                            }
                            CouncilCard("2", "Gemini · ผู้ตรวจสอบ", "ตรวจข้อผิดพลาดและโต้แย้งอย่างอิสระ") {
                                val prompt = "คุณคือ Gemini ผู้ตรวจสอบอิสระใน AI COUNCIL ตรวจสอบคำตอบ GPT ต่อไปนี้ จับข้อผิดพลาด ข้อมูลที่ขาด สมมติฐานที่ไม่สมเหตุผล และเสนอแนวทางที่ดีกว่า:\n\nโจทย์: $topic\n\nคำตอบ GPT:\n$gpt"
                                ActionButton("คัดลอกคำสั่งให้ Gemini") { clipboard.setText(AnnotatedString(prompt)) }
                                ActionButton("เปิด Gemini") { openUrl("https://gemini.google.com") }
                                OutlinedTextField(gemini, { gemini = it }, Modifier.fillMaxWidth(), minLines = 3, label = { Text("วางคำตอบจาก Gemini") })
                            }
                            CouncilCard("3", "GPT · ผู้สรุป", "พิจารณาความเห็นทั้งสองฝ่ายและสรุป") {
                                val prompt = "คุณคือ GPT ประธาน AI COUNCIL โปรดสรุปผลประชุมจากโจทย์ คำตอบ GPT และคำตรวจสอบ Gemini แยกข้อเท็จจริง ความเห็นที่เห็นตรงกัน ความเห็นที่ขัดแย้ง ความเสี่ยง และข้อสรุปสุดท้าย:\n\nโจทย์: $topic\n\nคำตอบแรก GPT:\n$gpt\n\nผลตรวจ Gemini:\n$gemini"
                                ActionButton("คัดลอกคำสั่งสรุป") { clipboard.setText(AnnotatedString(prompt)) }
                                ActionButton("เปิด ChatGPT เพื่อสรุป") { openUrl("https://chatgpt.com") }
                                OutlinedTextField(summary, { summary = it }, Modifier.fillMaxWidth(), minLines = 3, label = { Text("วางผลสรุปสุดท้าย") })
                                ActionButton("บันทึกการประชุมในเครื่อง") {
                                    prefs.edit().putString("last_topic", topic).putString("last_gpt", gpt)
                                        .putString("last_gemini", gemini).putString("last_summary", summary).apply()
                                    tab = "history"
                                }
                            }
                        } else {
                            Text("ประวัติการประชุมล่าสุด", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(12.dp))
                            Text("โจทย์: ${prefs.getString("last_topic", "ยังไม่มีข้อมูล")}", color = Muted)
                            Spacer(Modifier.height(8.dp))
                            Text("GPT: ${prefs.getString("last_gpt", "—")}")
                            Spacer(Modifier.height(8.dp))
                            Text("Gemini: ${prefs.getString("last_gemini", "—")}")
                            Spacer(Modifier.height(8.dp))
                            Text("สรุป: ${prefs.getString("last_summary", "—")}")
                            ActionButton("กลับไปหน้าประชุม") { tab = "home" }
                        }
                        Spacer(Modifier.height(24.dp))
                        Text("รุ่น 1.0 · ทำงานแบบคัดลอก/วาง · ไม่ใช้ API", color = Muted, fontSize = 11.sp)
                    }
                }
            }
        }
    }

    private fun openUrl(url: String) {
        runCatching { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
    }
}

@Composable
private fun CouncilCard(number: String, title: String, subtitle: String, content: @Composable ColumnScope.() -> Unit) {
    Card(Modifier.fillMaxWidth().padding(vertical = 8.dp), colors = CardDefaults.cardColors(containerColor = Panel)) {
        Column(Modifier.padding(14.dp), content = content) {
            Text("$number  $title", fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Muted, fontSize = 12.sp)
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun ActionButton(label: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) { Text(label) }
}
