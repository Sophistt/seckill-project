package com.xxxx.seckill.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MD5Util工具类的单元测试
 * 测试双重MD5加密的各个方法
 */
@SpringBootTest
class MD5UtilTest {

    @Test
    void testMd5BasicFunction() {
        // 测试基础MD5加密功能
        String input = "hello";
        String expected = "5d41402abc4b2a76b9719d911017c592";
        String actual = MD5Util.md5(input);
        
        assertEquals(expected, actual, "基础MD5加密应该产生预期的哈希值");
        
        // 测试空字符串
        String emptyInput = "";
        String emptyExpected = "d41d8cd98f00b204e9800998ecf8427e";
        String emptyActual = MD5Util.md5(emptyInput);
        assertEquals(emptyExpected, emptyActual, "空字符串的MD5加密应该正确");
        
        // 测试相同输入产生相同输出
        assertEquals(MD5Util.md5(input), MD5Util.md5(input), "相同输入应该产生相同的MD5哈希值");
    }

    @Test
    void testMd5WithNullInput() {
        // 测试null输入的处理
        assertThrows(NullPointerException.class, () -> {
            MD5Util.md5(null);
        }, "null输入应该抛出NullPointerException异常");
    }

    @Test
    void testInputPassToFormPass() {
        // 测试前端密码到表单密码的转换
        String inputPass = "123456";
        String expected = "ce21b747de5af71ab5c2e20ff0a60eea"; // 根据main方法中的输出
        String actual = MD5Util.inputPassToFormPass(inputPass);
        
        assertEquals(expected, actual, "前端密码转换应该产生预期的表单密码");
        
        // 测试不同输入
        String anotherInput = "password";
        String anotherResult = MD5Util.inputPassToFormPass(anotherInput);
        assertNotEquals(actual, anotherResult, "不同的输入应该产生不同的表单密码");
        
        // 测试一致性
        assertEquals(MD5Util.inputPassToFormPass(inputPass), MD5Util.inputPassToFormPass(inputPass), 
                    "相同输入应该产生相同的表单密码");
    }

    @Test
    void testInputPassToFormPassWithEmptyString() {
        // 测试空字符串输入
        String emptyInput = "";
        String result = MD5Util.inputPassToFormPass(emptyInput);
        assertNotNull(result, "空字符串输入应该返回有效的哈希值");
        assertEquals(32, result.length(), "MD5哈希值应该是32位十六进制字符串");
    }

    @Test
    void testFormPassToDBPass() {
        // 测试表单密码到数据库密码的转换
        String formPass = "ce21b747de5af71ab5c2e20ff0a60eea";
        String salt = "xiaochaoaidami";
        String expected = "fbbb652b77b85f999898b55e8f8eaab1"; // 根据main方法的实际输出
        String actual = MD5Util.formPassToDBPass(formPass, salt);
        
        assertEquals(expected, actual, "表单密码到数据库密码转换应该产生预期结果");
        assertNotNull(actual, "数据库密码不应该为null");
        assertEquals(32, actual.length(), "数据库密码应该是32位十六进制字符串");
        
        // 测试不同盐值产生不同结果
        String differentSalt = "anothersalt123";
        String differentResult = MD5Util.formPassToDBPass(formPass, differentSalt);
        assertNotEquals(actual, differentResult, "不同盐值应该产生不同的数据库密码");
        
        // 测试一致性
        assertEquals(MD5Util.formPassToDBPass(formPass, salt), MD5Util.formPassToDBPass(formPass, salt),
                    "相同输入和盐值应该产生相同的数据库密码");
    }

    @Test
    void testFormPassToDBPassWithEdgeCases() {
        String formPass = "abcdef1234567890abcdef1234567890"; // 32位字符串
        
        // 测试短盐值（少于6个字符）
        assertThrows(StringIndexOutOfBoundsException.class, () -> {
            MD5Util.formPassToDBPass(formPass, "abc");
        }, "盐值长度不足应该抛出异常");
        
        // 测试正好6个字符的盐值
        String sixCharSalt = "abcdef";
        assertDoesNotThrow(() -> {
            String result = MD5Util.formPassToDBPass(formPass, sixCharSalt);
            assertNotNull(result);
        }, "6个字符的盐值应该可以正常处理");
    }

    @Test
    void testInputPassToDBPass() {
        // 测试完整的双重加密流程
        String inputPass = "123456";
        String salt = "xiaochaoaidami";
        String expected = "fbbb652b77b85f999898b55e8f8eaab1"; // 根据main方法中的实际输出
        String actual = MD5Util.inputPassToDBPass(inputPass, salt);
        
        assertEquals(expected, actual, "完整双重加密应该产生预期的结果");
        
        // 验证它与分步加密的结果一致
        String stepByStep = MD5Util.formPassToDBPass(MD5Util.inputPassToFormPass(inputPass), salt);
        assertEquals(actual, stepByStep, "一步到位的加密应该与分步加密结果一致");
    }

    @Test
    void testInputPassToDBPassWithDifferentPasswords() {
        String salt = "testsalt123456";
        
        // 测试多个不同密码
        String[] passwords = {"123456", "password", "admin", "test@123", "复杂密码#123"};
        String[] results = new String[passwords.length];
        
        for (int i = 0; i < passwords.length; i++) {
            results[i] = MD5Util.inputPassToDBPass(passwords[i], salt);
            assertNotNull(results[i], "密码 " + passwords[i] + " 应该产生有效结果");
            assertEquals(32, results[i].length(), "结果应该是32位十六进制字符串");
        }
        
        // 确保所有结果都不相同
        for (int i = 0; i < results.length - 1; i++) {
            for (int j = i + 1; j < results.length; j++) {
                assertNotEquals(results[i], results[j], 
                    "不同密码应该产生不同的哈希值: " + passwords[i] + " vs " + passwords[j]);
            }
        }
    }

    @Test
    void testSaltPositionLogic() {
        // 测试盐值字符位置逻辑
        String testInput = "test";
        String testSalt = "abcdefghij"; // 长度>=6的盐值
        
        // 根据代码逻辑：salt.charAt(0) + salt.charAt(2) + input + salt.charAt(5) + salt.charAt(4)
        // 对于盐值"abcdefghij"，应该是：'a' + 'c' + testInput + 'f' + 'e'
        // 我们验证加密一致性而不是内部字符串构造
        
        String result1 = MD5Util.inputPassToFormPass(testInput);
        String result2 = MD5Util.inputPassToFormPass(testInput);
        assertEquals(result1, result2, "相同输入应该产生一致的结果");
        
        // 测试formPassToDBPass的盐值逻辑
        String formPass = "1234567890123456789012345678901a"; // 32位字符串
        String result3 = MD5Util.formPassToDBPass(formPass, testSalt);
        String result4 = MD5Util.formPassToDBPass(formPass, testSalt);
        assertEquals(result3, result4, "相同输入和盐值应该产生一致的结果");
    }

    @Test
    void testHashValueFormat() {
        // 测试哈希值格式
        String input = "testpassword";
        String salt = "testsalt123";
        
        String formPass = MD5Util.inputPassToFormPass(input);
        String dbPass = MD5Util.formPassToDBPass(formPass, salt);
        String directDbPass = MD5Util.inputPassToDBPass(input, salt);
        
        // 验证所有结果都是有效的MD5哈希格式
        assertTrue(formPass.matches("^[a-f0-9]{32}$"), "表单密码应该是32位小写十六进制字符串");
        assertTrue(dbPass.matches("^[a-f0-9]{32}$"), "数据库密码应该是32位小写十六进制字符串");
        assertTrue(directDbPass.matches("^[a-f0-9]{32}$"), "直接数据库密码应该是32位小写十六进制字符串");
    }

    @Test
    void testRealWorldScenario() {
        // 模拟真实使用场景
        String userInputPassword = "MySecureP@ssw0rd!";
        String userSalt = "randomSalt12345";
        
        // 步骤1：前端提交时的第一次加密
        String frontendEncrypted = MD5Util.inputPassToFormPass(userInputPassword);
        
        // 步骤2：后端存储时的第二次加密
        String dbEncrypted = MD5Util.formPassToDBPass(frontendEncrypted, userSalt);
        
        // 步骤3：验证完整流程
        String oneStepEncrypted = MD5Util.inputPassToDBPass(userInputPassword, userSalt);
        
        assertEquals(dbEncrypted, oneStepEncrypted, "分步加密和一步加密应该产生相同结果");
        
        // 验证安全性：原始密码不应该与任何加密结果相同
        assertNotEquals(userInputPassword, frontendEncrypted, "前端加密后应该与原密码不同");
        assertNotEquals(userInputPassword, dbEncrypted, "数据库加密后应该与原密码不同");
        assertNotEquals(frontendEncrypted, dbEncrypted, "两次加密结果应该不同");
    }
}