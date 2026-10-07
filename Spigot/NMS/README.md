# NMS Modülleri

Bu klasör, farklı Minecraft sürümleri için NMS (Net Minecraft Server) implementasyonlarını içerir. Her modül, belirli bir Minecraft sürümü (örn. 1.10.R1, 1.21.R4) için özelleştirilmiş kodları barındırır.

## SPI Mekanizması

Her NMS modülü, kendi SPI (Service Provider Interface) tanımlamasını içermelidir. Bu, uygun ParticleProvider sınıfının otomatik olarak yüklenmesini sağlar.

### Yeni bir NMS Modülü Eklemek

Yeni bir Minecraft sürümü için NMS modülü eklemek istiyorsanız:

1. İlgili sürüm için yeni bir alt modül oluşturun (örn. V1_XX_RX)
2. ParticleProvider sınıfını implement edin
3. `src/main/resources/META-INF/services/me.erano.com.api.particle.ParticleAPI` dosyasını oluşturun
4. Bu dosyaya ParticleProvider sınıfınızın tam yolunu ekleyin

### Örnek SPI Tanımlaması

```
me.erano.com.nms.v1_XX_RX.ParticleProvider
```

## Dist Modülü

Tüm NMS modüllerinin bir araya getirildiği Dist modülü, Maven Shade Plugin ile birlikte ServicesResourceTransformer kullanarak tüm SPI tanımlamalarını birleştirir. Bu sayede tüm sürümler için tek bir JAR dosyası oluşturulur.
