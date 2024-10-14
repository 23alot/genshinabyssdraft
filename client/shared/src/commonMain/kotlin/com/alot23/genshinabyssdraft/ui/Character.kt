@file:OptIn(ExperimentalResourceApi::class, ExperimentalResourceApi::class, ExperimentalResourceApi::class)

package com.alot23.genshinabyssdraft.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alot23.genshinabyssdraft.client.shared.generated.resources.Res
import com.alot23.genshinabyssdraft.client.shared.generated.resources.*
import com.alot23.genshinabyssdraft.entity.CharacterConfiguration
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun Character(characterInfo: CharacterInfo, clickable: Boolean, onClick: ((CharacterInfo) -> Unit)) = Box(
    modifier = Modifier.width(80.dp).height(80.dp).clickable(enabled = clickable) { onClick(characterInfo) },
) {
    Image(
        painter = painterResource(characterInfo.character.icon),
        contentDescription = null,
        modifier = Modifier.height(height = 72.dp).width(72.dp).clip(shape = CircleShape).background(color = characterInfo.character.element.toColor()).align(Alignment.TopCenter)
    )
    if (characterInfo.isDisabled) {
        Box(modifier = Modifier.height(height = 72.dp).width(72.dp).clip(shape = CircleShape).background(color = Color(0,0,0, 125)).align(Alignment.TopCenter))
    } else {
        Row(modifier = Modifier.clip(shape = RoundedCornerShape(size = 8.dp)).background(color = Color.White).align(Alignment.BottomCenter).height(24.dp)) {
            Text(text = "${characterInfo.level}", modifier = Modifier.width(32.dp).padding(vertical = 1.dp, horizontal = 4.dp).align(Alignment.CenterVertically), textAlign = TextAlign.End)
            Box(modifier = Modifier.fillMaxHeight().width(1.dp).background(color = Color.Black))
            Text(text = "${characterInfo.constellations}", modifier = Modifier.width(32.dp).padding(vertical = 1.dp, horizontal = 4.dp).align(Alignment.CenterVertically), textAlign = TextAlign.Start)
        }
    }


}

data class CharacterInfo(
    val character: GameCharacter,
    val constellations: Int,
    val level: Int,
    val isDisabled: Boolean = false
)

sealed interface Element {

    data object Electro : Element

    data object Hydro : Element

    data object Pyro : Element

    data object Cryo : Element

    data object Geo : Element

    data object Anemo : Element

    data object Dendro : Element

}

fun Element.toColor(): Color = when (this) {
    is Element.Electro -> Color(0xFF8f73bc)
    is Element.Hydro -> Color(0xFF1eafc2)
    is Element.Pyro -> Color(0xFFc57861)
    is Element.Anemo -> Color(0xFF85be9f)
    is Element.Cryo -> Color(0xFFa5d4d4)
    is Element.Dendro -> Color(0xFFb2ea2a)
    is Element.Geo -> Color(0xFFa19045)
}

sealed interface GameCharacter {
    val name: String
    val element: Element
    val icon: DrawableResource
    val rare: Int

    data object Albedo : GameCharacter {
        override val name: String = "Albedo"
        override val element: Element = Element.Geo
        override val icon: DrawableResource = Res.drawable.albedo
        override val rare: Int = 5
    }
    data object Diluc : GameCharacter {
        override val name: String = "Diluc"
        override val element: Element = Element.Pyro
        override val icon: DrawableResource = Res.drawable.diluc
        override val rare: Int = 5
    }
    data object Kaveh : GameCharacter {
        override val name: String = "Kaveh"
        override val element: Element = Element.Dendro
        override val icon: DrawableResource = Res.drawable.kaveh
        override val rare: Int = 4
    }
    data object Ningguang : GameCharacter {
        override val name: String = "Ningguang"
        override val element: Element = Element.Geo
        override val icon: DrawableResource = Res.drawable.ningguang
        override val rare: Int = 4
    }
    data object Tohma : GameCharacter {
        override val name: String = "Tohma"
        override val element: Element = Element.Pyro
        override val icon: DrawableResource = Res.drawable.tohma
        override val rare: Int = 4
    }
    data object Alhatham : GameCharacter {
        override val name: String = "Alhatham"
        override val element: Element = Element.Dendro
        override val icon: DrawableResource = Res.drawable.alhatham
        override val rare: Int = 5
    }
    data object Diona : GameCharacter {
        override val name: String = "Diona"
        override val element: Element = Element.Cryo
        override val icon: DrawableResource = Res.drawable.diona
        override val rare: Int = 4
    }
    data object Kazuha : GameCharacter {
        override val name: String = "Kazuha"
        override val element: Element = Element.Anemo
        override val icon: DrawableResource = Res.drawable.kazuha
        override val rare: Int = 5
    }
    data object Noel : GameCharacter {
        override val name: String = "Noel"
        override val element: Element = Element.Geo
        override val icon: DrawableResource = Res.drawable.noel
        override val rare: Int = 4
    }
    data object Venti : GameCharacter {
        override val name: String = "Venti"
        override val element: Element = Element.Anemo
        override val icon: DrawableResource = Res.drawable.venti
        override val rare: Int = 5
    }
    data object Aloy : GameCharacter {
        override val name: String = "Aloy"
        override val element: Element = Element.Cryo
        override val icon: DrawableResource = Res.drawable.aloy
        override val rare: Int = 5
    }
    data object Dori : GameCharacter {
        override val name: String = "Dori"
        override val element: Element = Element.Electro
        override val icon: DrawableResource = Res.drawable.dori
        override val rare: Int = 4
    }
    data object Keqing : GameCharacter {
        override val name: String = "Keqing"
        override val element: Element = Element.Electro
        override val icon: DrawableResource = Res.drawable.keqing
        override val rare: Int = 5
    }
    data object PlayerboyAnemo : GameCharacter {
        override val name: String = "PlayerboyAnemo"
        override val element: Element = Element.Anemo
        override val icon: DrawableResource = Res.drawable.playerboy
        override val rare: Int = 5
    }
    data object PlayerboyGeo : GameCharacter {
        override val name: String = "PlayerboyGeo"
        override val element: Element = Element.Geo
        override val icon: DrawableResource = Res.drawable.playerboy
        override val rare: Int = 5
    }
    data object PlayerboyElectro : GameCharacter {
        override val name: String = "PlayerboyElectro"
        override val element: Element = Element.Electro
        override val icon: DrawableResource = Res.drawable.playerboy
        override val rare: Int = 5
    }
    data object PlayerboyDendro : GameCharacter {
        override val name: String = "PlayerboyDendro"
        override val element: Element = Element.Dendro
        override val icon: DrawableResource = Res.drawable.playerboy
        override val rare: Int = 5
    }
    data object PlayerboyHydro : GameCharacter {
        override val name: String = "PlayerboyHydro"
        override val element: Element = Element.Hydro
        override val icon: DrawableResource = Res.drawable.playerboy
        override val rare: Int = 5
    }

    data object Wanderer : GameCharacter {
        override val name: String = "Wanderer"
        override val element: Element = Element.Anemo
        override val icon: DrawableResource = Res.drawable.wanderer
        override val rare: Int = 5
    }
    data object Amber : GameCharacter {
        override val name: String = "Ambor"
        override val element: Element = Element.Pyro
        override val icon: DrawableResource = Res.drawable.amber
        override val rare: Int = 4
    }
    data object Eula : GameCharacter {
        override val name: String = "Eula"
        override val element: Element = Element.Cryo
        override val icon: DrawableResource = Res.drawable.eula
        override val rare: Int = 5
    }
    data object Klee : GameCharacter {
        override val name: String = "Klee"
        override val element: Element = Element.Pyro
        override val icon: DrawableResource = Res.drawable.klee
        override val rare: Int = 5
    }
    data object Wriothesley : GameCharacter {
        override val name: String = "Wriothesley"
        override val element: Element = Element.Cryo
        override val icon: DrawableResource = Res.drawable.wriothesley
        override val rare: Int = 5
    }
    data object Ayaka : GameCharacter {
        override val name: String = "Ayaka"
        override val element: Element = Element.Cryo
        override val icon: DrawableResource = Res.drawable.ayaka
        override val rare: Int = 5
    }
    data object Faruzan : GameCharacter {
        override val name: String = "Faruzan"
        override val element: Element = Element.Anemo
        override val icon: DrawableResource = Res.drawable.faruzan
        override val rare: Int = 4
    }
    data object Kokomi : GameCharacter {
        override val name: String = "Kokomi"
        override val element: Element = Element.Hydro
        override val icon: DrawableResource = Res.drawable.kokomi
        override val rare: Int = 5
    }
    data object Qin : GameCharacter {
        override val name: String = "Qin"
        override val element: Element = Element.Anemo
        override val icon: DrawableResource = Res.drawable.qin
        override val rare: Int = 5
    }
    data object Xiangling : GameCharacter {
        override val name: String = "Xiangling"
        override val element: Element = Element.Pyro
        override val icon: DrawableResource = Res.drawable.xiangling
        override val rare: Int = 4
    }
    data object Ayato : GameCharacter {
        override val name: String = "Ayato"
        override val element: Element = Element.Hydro
        override val icon: DrawableResource = Res.drawable.ayato
        override val rare: Int = 5
    }
    data object Feiyan : GameCharacter {
        override val name: String = "Feiyan"
        override val element: Element = Element.Pyro
        override val icon: DrawableResource = Res.drawable.feiyan
        override val rare: Int = 4
    }
    data object Layla : GameCharacter {
        override val name: String = "Layla"
        override val element: Element = Element.Cryo
        override val icon: DrawableResource = Res.drawable.layla
        override val rare: Int = 4
    }
    data object Qiqi : GameCharacter {
        override val name: String = "Qiqi"
        override val element: Element = Element.Cryo
        override val icon: DrawableResource = Res.drawable.qiqi
        override val rare: Int = 5
    }
    data object Xiao : GameCharacter {
        override val name: String = "Xiao"
        override val element: Element = Element.Anemo
        override val icon: DrawableResource = Res.drawable.xiao
        override val rare: Int = 5
    }
    data object Baizhuer : GameCharacter {
        override val name: String = "Baizhuer"
        override val element: Element = Element.Dendro
        override val icon: DrawableResource = Res.drawable.baizhuer
        override val rare: Int = 5
    }
    data object Fischl : GameCharacter {
        override val name: String = "Fischl"
        override val element: Element = Element.Electro
        override val icon: DrawableResource = Res.drawable.fischl
        override val rare: Int = 4
    }
    data object Linette : GameCharacter {
        override val name: String = "Linette"
        override val element: Element = Element.Anemo
        override val icon: DrawableResource = Res.drawable.linette
        override val rare: Int = 4
    }
    data object Razor : GameCharacter {
        override val name: String = "Razor"
        override val element: Element = Element.Electro
        override val icon: DrawableResource = Res.drawable.razor
        override val rare: Int = 4
    }
    data object Xingqiu : GameCharacter {
        override val name: String = "Xingqiu"
        override val element: Element = Element.Hydro
        override val icon: DrawableResource = Res.drawable.xingqiu
        override val rare: Int = 4
    }
    data object Barbara : GameCharacter {
        override val name: String = "Barbara"
        override val element: Element = Element.Hydro
        override val icon: DrawableResource = Res.drawable.barbara
        override val rare: Int = 4
    }
    data object Freminet : GameCharacter {
        override val name: String = "Freminet"
        override val element: Element = Element.Cryo
        override val icon: DrawableResource = Res.drawable.freminet
        override val rare: Int = 4
    }
    data object Liney : GameCharacter {
        override val name: String = "Liney"
        override val element: Element = Element.Pyro
        override val icon: DrawableResource = Res.drawable.liney
        override val rare: Int = 5
    }
    data object Rosaria : GameCharacter {
        override val name: String = "Rosaria"
        override val element: Element = Element.Cryo
        override val icon: DrawableResource = Res.drawable.rosaria
        override val rare: Int = 4
    }
    data object Xinyan : GameCharacter {
        override val name: String = "Xinyan"
        override val element: Element = Element.Pyro
        override val icon: DrawableResource = Res.drawable.xinyan
        override val rare: Int = 4
    }
    data object Beidou : GameCharacter {
        override val name: String = "Beidou"
        override val element: Element = Element.Electro
        override val icon: DrawableResource = Res.drawable.beidou
        override val rare: Int = 4
    }
    data object Furina : GameCharacter {
        override val name: String = "Furina"
        override val element: Element = Element.Hydro
        override val icon: DrawableResource = Res.drawable.furina
        override val rare: Int = 5
    }
    data object Lisa : GameCharacter {
        override val name: String = "Lisa"
        override val element: Element = Element.Electro
        override val icon: DrawableResource = Res.drawable.lisa
        override val rare: Int = 4
    }
    data object Sara : GameCharacter {
        override val name: String = "Sara"
        override val element: Element = Element.Electro
        override val icon: DrawableResource = Res.drawable.sara
        override val rare: Int = 4
    }
    data object Yae : GameCharacter {
        override val name: String = "Yae"
        override val element: Element = Element.Electro
        override val icon: DrawableResource = Res.drawable.yae
        override val rare: Int = 5
    }
    data object Bennett : GameCharacter {
        override val name: String = "Bennett"
        override val element: Element = Element.Pyro
        override val icon: DrawableResource = Res.drawable.bennett
        override val rare: Int = 4
    }
    data object Gaming : GameCharacter {
        override val name: String = "Gaming"
        override val element: Element = Element.Pyro
        override val icon: DrawableResource = Res.drawable.gaming
        override val rare: Int = 4
    }
    data object Liuyun : GameCharacter {
        override val name: String = "Liuyun"
        override val element: Element = Element.Anemo
        override val icon: DrawableResource = Res.drawable.liuyun
        override val rare: Int = 5
    }
    data object Sayu : GameCharacter {
        override val name: String = "Sayu"
        override val element: Element = Element.Anemo
        override val icon: DrawableResource = Res.drawable.sayu
        override val rare: Int = 4
    }
    data object Yaoyao : GameCharacter {
        override val name: String = "Yaoyao"
        override val element: Element = Element.Dendro
        override val icon: DrawableResource = Res.drawable.yaoyao
        override val rare: Int = 4
    }
    data object Candace : GameCharacter {
        override val name: String = "Candace"
        override val element: Element = Element.Hydro
        override val icon: DrawableResource = Res.drawable.candace
        override val rare: Int = 4
    }
    data object Ganyu : GameCharacter {
        override val name: String = "Ganyu"
        override val element: Element = Element.Cryo
        override val icon: DrawableResource = Res.drawable.ganyu
        override val rare: Int = 5
    }
    data object Mika : GameCharacter {
        override val name: String = "Mika"
        override val element: Element = Element.Cryo
        override val icon: DrawableResource = Res.drawable.mika
        override val rare: Int = 4
    }
    data object Shenhe : GameCharacter {
        override val name: String = "Shenhe"
        override val element: Element = Element.Cryo
        override val icon: DrawableResource = Res.drawable.shenhe
        override val rare: Int = 5
    }
    data object Yelan : GameCharacter {
        override val name: String = "Yelan"
        override val element: Element = Element.Hydro
        override val icon: DrawableResource = Res.drawable.yelan
        override val rare: Int = 5
    }
    data object Charlotte : GameCharacter {
        override val name: String = "Charlotte"
        override val element: Element = Element.Cryo
        override val icon: DrawableResource = Res.drawable.charlotte
        override val rare: Int = 4
    }
    data object Gorou : GameCharacter {
        override val name: String = "Gorou"
        override val element: Element = Element.Geo
        override val icon: DrawableResource = Res.drawable.gorou
        override val rare: Int = 4
    }
    data object Momoka : GameCharacter {
        override val name: String = "Momoka"
        override val element: Element = Element.Dendro
        override val icon: DrawableResource = Res.drawable.momoka
        override val rare: Int = 4
    }
    data object Shinobu : GameCharacter {
        override val name: String = "Shinobu"
        override val element: Element = Element.Electro
        override val icon: DrawableResource = Res.drawable.shinobu
        override val rare: Int = 4
    }
    data object Yoimiya : GameCharacter {
        override val name: String = "Yoimiya"
        override val element: Element = Element.Pyro
        override val icon: DrawableResource = Res.drawable.yoimiya
        override val rare: Int = 5
    }
    data object Chongyun : GameCharacter {
        override val name: String = "Chongyun"
        override val element: Element = Element.Cryo
        override val icon: DrawableResource = Res.drawable.chongyun
        override val rare: Int = 4
    }
    data object Heizo : GameCharacter {
        override val name: String = "Heizo"
        override val element: Element = Element.Anemo
        override val icon: DrawableResource = Res.drawable.heizo
        override val rare: Int = 4
    }
    data object Mona : GameCharacter {
        override val name: String = "Mona"
        override val element: Element = Element.Hydro
        override val icon: DrawableResource = Res.drawable.mona
        override val rare: Int = 5
    }
    data object Shougun : GameCharacter {
        override val name: String = "Shougun"
        override val element: Element = Element.Electro
        override val icon: DrawableResource = Res.drawable.shougun
        override val rare: Int = 5
    }
    data object Yunjin : GameCharacter {
        override val name: String = "Yunjin"
        override val element: Element = Element.Geo
        override val icon: DrawableResource = Res.drawable.yunjin
        override val rare: Int = 4
    }
    data object Collei : GameCharacter {
        override val name: String = "Collei"
        override val element: Element = Element.Dendro
        override val icon: DrawableResource = Res.drawable.collei
        override val rare: Int = 4
    }
    data object Hutao : GameCharacter {
        override val name: String = "Hutao"
        override val element: Element = Element.Pyro
        override val icon: DrawableResource = Res.drawable.hutao
        override val rare: Int = 5
    }
    data object Nahida : GameCharacter {
        override val name: String = "Nahida"
        override val element: Element = Element.Dendro
        override val icon: DrawableResource = Res.drawable.nahida
        override val rare: Int = 5
    }
    data object Sucrose : GameCharacter {
        override val name: String = "Sucrose"
        override val element: Element = Element.Anemo
        override val icon: DrawableResource = Res.drawable.sucrose
        override val rare: Int = 4
    }
    data object Zhongli : GameCharacter {
        override val name: String = "Zhongli"
        override val element: Element = Element.Geo
        override val icon: DrawableResource = Res.drawable.zhongli
        override val rare: Int = 5
    }
    data object Cyno : GameCharacter {
        override val name: String = "Cyno"
        override val element: Element = Element.Electro
        override val icon: DrawableResource = Res.drawable.cyno
        override val rare: Int = 5
    }
    data object Itto : GameCharacter {
        override val name: String = "Itto"
        override val element: Element = Element.Geo
        override val icon: DrawableResource = Res.drawable.itto
        override val rare: Int = 5
    }
    data object Neuvillette : GameCharacter {
        override val name: String = "Neuvillette"
        override val element: Element = Element.Hydro
        override val icon: DrawableResource = Res.drawable.neuvillette
        override val rare: Int = 5
    }
    data object Tartaglia : GameCharacter {
        override val name: String = "Tartaglia"
        override val element: Element = Element.Hydro
        override val icon: DrawableResource = Res.drawable.tartaglia
        override val rare: Int = 5
    }
    data object Dehya : GameCharacter {
        override val name: String = "Dehya"
        override val element: Element = Element.Pyro
        override val icon: DrawableResource = Res.drawable.dehya
        override val rare: Int = 5
    }
    data object Kaeya : GameCharacter {
        override val name: String = "Kaeya"
        override val element: Element = Element.Cryo
        override val icon: DrawableResource = Res.drawable.kaeya
        override val rare: Int = 4
    }
    data object Nilou : GameCharacter {
        override val name: String = "Nilou"
        override val element: Element = Element.Hydro
        override val icon: DrawableResource = Res.drawable.nilou
        override val rare: Int = 5
    }
    data object Tighnari : GameCharacter {
        override val name: String = "Tighnari"
        override val element: Element = Element.Dendro
        override val icon: DrawableResource = Res.drawable.tighnari
        override val rare: Int = 5
    }
    data object Navia : GameCharacter {
        override val name: String = "Navia"
        override val element: Element = Element.Geo
        override val icon: DrawableResource = Res.drawable.navia
        override val rare: Int = 5
    }
    data object Chevreuse : GameCharacter {
        override val name: String = "Chevreuse"
        override val element: Element = Element.Pyro
        override val icon: DrawableResource = Res.drawable.chevreuse
        override val rare: Int = 5
    }
    data object Chiori : GameCharacter {
        override val name: String = "Chiori"
        override val element: Element = Element.Geo
        override val icon: DrawableResource = Res.drawable.chiori
        override val rare: Int = 5
    }
}

fun gameCharacterValues(): List<GameCharacter> = listOf(GameCharacter.Albedo, GameCharacter.Amber, GameCharacter.Barbara, GameCharacter.Beidou, GameCharacter.Bennett, GameCharacter.Chongyun, GameCharacter.Diluc, GameCharacter.Diona, GameCharacter.Fischl, GameCharacter.Ganyu, GameCharacter.Hutao, GameCharacter.Qin, GameCharacter.Kaeya, GameCharacter.Keqing, GameCharacter.Klee, GameCharacter.Lisa, GameCharacter.Mona, GameCharacter.Ningguang, GameCharacter.Noel, GameCharacter.Qiqi, GameCharacter.Razor, GameCharacter.Sucrose, GameCharacter.Tartaglia, GameCharacter.PlayerboyAnemo, GameCharacter.PlayerboyGeo, GameCharacter.Venti, GameCharacter.Xiangling, GameCharacter.Xiao, GameCharacter.Xingqiu, GameCharacter.Xinyan, GameCharacter.Zhongli, GameCharacter.Rosaria, GameCharacter.Eula, GameCharacter.Feiyan, GameCharacter.Kazuha, GameCharacter.Ayaka, GameCharacter.Yoimiya, GameCharacter.Sayu, GameCharacter.PlayerboyElectro, GameCharacter.Shougun, GameCharacter.Kokomi, GameCharacter.Sara, GameCharacter.Aloy, GameCharacter.Tohma, GameCharacter.Gorou, GameCharacter.Itto, GameCharacter.Yunjin, GameCharacter.Shenhe, GameCharacter.Yae, GameCharacter.Ayato, GameCharacter.Yelan, GameCharacter.Shinobu, GameCharacter.Heizo, GameCharacter.Tighnari, GameCharacter.Collei, GameCharacter.Dori, GameCharacter.PlayerboyDendro, GameCharacter.Candace, GameCharacter.Cyno, GameCharacter.Nilou, GameCharacter.Nahida, GameCharacter.Layla, GameCharacter.Wanderer, GameCharacter.Faruzan, GameCharacter.Yaoyao, GameCharacter.Alhatham, GameCharacter.Dehya, GameCharacter.Mika, GameCharacter.Kaveh, GameCharacter.Baizhuer, GameCharacter.Momoka, GameCharacter.Linette, GameCharacter.Liney, GameCharacter.Freminet, GameCharacter.PlayerboyHydro, GameCharacter.Wriothesley, GameCharacter.Neuvillette, GameCharacter.Charlotte, GameCharacter.Furina, GameCharacter.Chevreuse, GameCharacter.Navia, GameCharacter.Gaming, GameCharacter.Liuyun, GameCharacter.Chiori)

fun CharacterConfiguration.toCharacterInfo(immuneCharacters: List<String>): CharacterInfo {
    val position = this.name.toInt() - 1
    val character = gameCharacterValues()[position]
    return CharacterInfo(
        character = character,
        constellations = constellations,
        level = level,
        isDisabled = character.name in immuneCharacters
    )
}