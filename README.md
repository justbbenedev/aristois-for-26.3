# EMC + Aristois en Minecraft 26.3, versión que no crashea 🛠️

> *EN: Fork of the community EMC port for Minecraft 26.3 that fixes the crash when opening the Aristois menu. Instructions below are in Spanish, but the steps are the same.*

Probé el port de la comunidad de **EMC (Easy Minecraft Client)** para 26.3. El juego arrancaba, y apenas Aristois quería dibujar su menú, chau:

```
java.lang.NoSuchFieldError: Class ... does not have member field 'int glId'
```

Así que le metí mano. Este repo es ese port con el arreglo.

## Qué estaba roto

`Aristois.jar` está compilado contra EMC 1.21.4. Ahí `GlTexture.glId` era un `int`, el id de la textura en OpenGL. Con el renderer nuevo de Minecraft, el port a 26.3 lo cambió a `GpuTexture`. Aristois busca el campo `int glId`, no lo encuentra y crashea.

**El arreglo:** la textura nueva se guarda en `gpuTexture` (y `getGlId()` la sigue devolviendo), y vuelve `int glId` para que Aristois sepa si la textura está lista: `0` = no, distinto de `0` = sí. Es un cambio de 20 líneas en [`GlTexture.java`](src/main/java/me/deftware/client/framework/render/texture/GlTexture.java).

También comparé todo lo que `Aristois.jar` le pide a EMC (campos y métodos) contra este EMC. Esa era la única pieza que no coincidía.

## Instalación

Necesitás Minecraft **26.3** (abrilo una vez sin mods), **Java 25** y tres archivos:

| Archivo | De dónde | A dónde va |
|---|---|---|
| `EMC-F-v2-latest-26.3.jar` | [Releases de este repo](../../releases) | `.minecraft/libraries/me/deftware/EMC-F-v2/latest-26.3/` |
| `26.3-Aristois.json` | [Releases de este repo](../../releases) | `.minecraft/versions/26.3-Aristois/` |
| `Aristois.jar` | [aristois.net](https://aristois.net/download) o el [release de memelet11](https://github.com/memelet11/aristois-26.3-minecraft-release/releases) | `.minecraft/libraries/EMC/26.3/` |

Después reiniciá el launcher, elegí **26.3-Aristois** y dale a jugar. Aristois se abre con **Shift derecho**.

> `Aristois.jar` no está en este repo a propósito: es el cliente de Deftware, cerrado y sin licencia que permita redistribuirlo. Acá va solo EMC, que es MIT.

## Compilar

```bash
export JAVA_HOME=/ruta/a/jdk-25
./gradlew build
# sale en build/libs/EMC-17.0.0.jar → renombralo a EMC-F-v2-latest-26.3.jar
```

## Ojo

- En la mayoría de los servidores los clientes como Aristois están prohibidos y te pueden banear. Usalo en tus mundos.
- Lo de Aristois que se rompió entre 1.21.4 y 26.3 por dentro no lo puedo arreglar, porque es código cerrado. Si algo falla, abrí un issue con el crash report.

## Créditos

- [Deftware](https://gitlab.com/EMC-Framework/EMC): EMC y Aristois
- [memelet11](https://github.com/memelet11/aristois-26.3-minecraft-release): el port de EMC a 26.3
- yo: el arreglo de `glId` y mucho mate 🧉

Licencia MIT, igual que EMC (ver [LICENSE](LICENSE)).
