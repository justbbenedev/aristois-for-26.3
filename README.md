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

## Novedades (fix 2): Aristois funciona de verdad en 26.3

Probado dentro del juego: el menú abre, los módulos se prenden y se apagan, X-ray esconde y muestra, y la lista de bloques del X-ray se puede editar.

- **Menú sin crash:** Aristois deja áreas de recorte de 0 px o fuera de pantalla y 26.3 crasheaba. Ahora se recortan a la pantalla y, si quedan vacías, ese draw se saltea (`MixinGuiRendererScissor`).
- **X-ray que no deja el mundo vacío:** `invalidateCompiledGeometry` tiraba la geometría sin rearmarla. Ahora usa `levelExtractor.allChanged()`, igual que F3+A.
- **X-ray con la lista correcta:** se vuelve a cargar el mundo cuando cambia la lista de bloques, porque Aristois la carga después de activarse.
- **X-ray con Fabric API:** Indigo arma los chunks por su cuenta y salteaba X-ray. `MixinIndigoBlockRenderer` aplica la misma lógica ahí (es opcional y no rompe nada si no hay Fabric API).
- **Carpeta de mods propia:** si existe `<.minecraft>/mods-aristois`, el perfil la usa. Ahí no van Sodium ni Iris, que reemplazan el renderizado y rompen X-ray.

### Un solo archivo (uso personal)

```bash
./gradlew build -ParistoisJar=/ruta/a/Aristois.jar
# build/libs/EMC-<ver>-aristois.jar trae Aristois adentro y lo extrae al arrancar
```

Este jar **no** se publica, porque Aristois es de Deftware. Armalo con tu propio `Aristois.jar`.

### Requisitos

- Gráficos en **OpenGL** (Opciones → Video). Con Vulkan, Aristois cierra el juego.
- Sin Sodium ni Iris en el perfil de Aristois.

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
