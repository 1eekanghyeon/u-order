package com.example.uwb

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : ComponentActivity() {
    // FirebaseAuth 인스턴스를 클래스 내부에 멤버 변수로 선언
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // FirebaseAuth 인스턴스 초기화
        auth = FirebaseAuth.getInstance()

        setContent {
            MaterialTheme {
                LoginScreen(
                    onLogin = { email, password ->
                        signInWithEmail(email, password)
                    },
                    onSignUp = {
                        // 회원가입 화면으로 이동
                        startActivity(Intent(this, SignUpActivity::class.java))
                    }
                )
            }
        }
    }

    // 로그인 로직
    private fun signInWithEmail(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    // 로그인 성공 시 MainActivity로 이동
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "로그인 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

@Composable
fun LoginScreen(onLogin: (String, String) -> Unit, onSignUp: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 로그인 버튼
        Button(
            onClick = {
                onLogin(email, password) // 로그인 로직 실행
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "로그인")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 회원가입 버튼
        Button(
            onClick = {
                onSignUp() // 회원가입 화면으로 이동
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "회원가입")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(onLogin = { _, _ -> }, onSignUp = {})
    }
}
