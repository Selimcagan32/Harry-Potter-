# 🧙‍♂️ Harry Potter Character Manager (Java Console App)

Bu proje, [HP-API](https://hp-api.onrender.com/) üzerinden Harry Potter karakter verilerini çeken, bu verileri yerel bir metin dosyasında (`karakterler.txt`) saklayan ve üzerinde temel CRUD (Oluşturma, Okuma, Güncelleme, Silme) işlemleri yapılmasına olanak tanıyan bir Java konsol uygulamasıdır.

## ✨ Özellikler

* **API Entegrasyonu:** HP-API'den HTTP GET isteği ile canlı karakter verilerini çekme.
* **Dosya İşlemleri (I/O):** Çekilen verileri `UTF-8` formatında ayrıştırarak yerel metin dosyasına kaydetme ve okuma.
* **Listeleme ve Filtreleme:** Tüm karakterleri listeleme veya Ev (House) / Patronus bilgilerine göre dinamik filtreleme yapabilme.
* **Kayıt Yönetimi:** Dosya içindeki spesifik karakter bilgilerini bularak güncelleme veya silme işlemleri.
* **Favori Sistemi:** Seçilen karakterleri farklı bir `favoriler.txt` dosyasında saklama.
* **Yedekleme:** Mevcut veri dosyasının kopyasını oluşturarak yedek alma (`karakterler_yedek.txt`).
* **Loglama:** API bağlantı hatalarını ve sistem istisnalarını zaman damgası (timestamp) ile birlikte `log.txt` dosyasına yazma.

## 🚀 Kurulum ve Kullanım

1. Projeyi bilgisayarınıza klonlayın:
    `git clone https://github.com/KULLANICI_ADIN/proje-repo-adi.git`

2. Terminal veya komut satırından proje dizinine gidin ve Java dosyasını derleyin:
    `javac proje2.java`

3. Uygulamayı çalıştırın:
    `java proje2`

*(Not: Test amaçlı varsayılan giriş şifresi `1234` olarak ayarlanmıştır.)*

## 🛠️ Kullanılan Teknolojiler

* **Dil:** Java (JDK 8 veya üzeri)
* **Ağ (Network):** `java.net.HttpURLConnection`, `java.net.URL`
* **Dosya Yönetimi:** `java.io.*`, `java.nio.file.*`
* **Veri Kaynağı:** [HP-API](https://hp-api.onrender.com/)
