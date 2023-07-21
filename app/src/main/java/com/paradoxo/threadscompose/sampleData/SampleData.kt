package com.paradoxo.threadscompose.sampleData

import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.model.Notification
import com.paradoxo.threadscompose.model.NotificationType
import com.paradoxo.threadscompose.model.Post
import com.paradoxo.threadscompose.model.UserAccount
import com.paradoxo.threadscompose.utils.getCurrentTime
import java.util.UUID

import kotlin.random.Random

class SampleData {
    val notifications = mutableListOf<Notification>()
    val posts = mutableListOf<Post>()
    val userAccounts = mutableListOf<UserAccount>()

    private val descriptions = listOf(
        "Viver o momento é a verdadeira essência da vida!",
        "Tirando um tempo para admirar as pequenas coisas da vida.",
        "A melhor parte da vida são as pessoas que conhecemos ao longo do caminho.",
        "A natureza sempre veste as cores do espírito.",
        "Acredite em si mesmo e tudo é possível.",
        "Sonhe grande e ouse falhar.",
        "Desfrutando de alguns dos meus momentos favoritos.",
        "Faça o que você ama, ame o que você faz.",
        "É sempre melhor quando estamos juntos.",
        "Inspire-se, mas seja você mesmo!"
    )

    private val bios = listOf(
        "Apaixonado por viagens e fotografia.",
        "Viciado em café e aventuras.",
        "Admirador da beleza da natureza.",
        "Colecionador de momentos, não de coisas.",
        "Um sonhador que se recusa a acordar.",
        "Vivendo um dia de cada vez.",
        "Amante de todas as coisas belas e extraordinárias.",
        "Em busca de inspiração e felicidade.",
        "Buscando a magia em cada dia.",
        "Vivendo a vida ao máximo!"
    )

    private val imagens = listOf(
        R.drawable.profile_pic_emoji_1,
        R.drawable.profile_pic_emoji_2,
        R.drawable.profile_pic_emoji_3,
        R.drawable.profile_pic_emoji_4,
    )

    init {
        val randomNumbers = mutableListOf<Long>()
        repeat(1000) {
            randomNumbers.add(it.toLong())
        }

        for (i in 1..10) {
            val userAccount = UserAccount(
                id = i.toLong(),
                name = "Nome $i",
                userName = "usuario$i",
                bio = bios[i - 1],
                imageProfileUrl = imagens.random(),
                posts = randomNumbers.subList(0, Random.nextInt(1, 1000)),
                follows = randomNumbers.subList(0, Random.nextInt(1, 1000)),
                followers = randomNumbers.subList(0, Random.nextInt(1, 1000)),
            )
            userAccounts.add(userAccount)

            val post = Post(
                id = i,
                userAccount = userAccount,
                description = descriptions[i - 1],
                date = getCurrentTime(),
                medias = if (Random.nextBoolean()) imagens.shuffled() else listOf(),
                likes = generateSampleLikes(),
                comments = generateSampleComments()
            )
            posts.add(post)
        }

        for (i in 1..10) {
            userAccounts[i - 1] =
                userAccounts[i - 1].copy(posts = posts.filter { it.userAccount.id == i.toLong() }
                    .map { it.id.toLong() })
        }

        repeat(10) {
            notifications.add(
                Notification(
                    id = UUID.randomUUID().toString(),
                    title = "Titulo $it",
                    description = "Descrição $it",
                    extraContent = if (Random.nextBoolean()) "Conteúdo extra $it" else null,
                    image = R.drawable.profile_pic_emoji_4,
                    time = "1d",
                    type = if (it in 2..4) NotificationType.values().sortedArray()[it] else
                        NotificationType.values().sortedArray()[Random.nextInt(0, 5)],
                    isFollowing = Random.nextBoolean(),
                )
            )
        }

    }

    private fun generateSampleLikes(): List<Long> {
        if (Random.nextBoolean()) return emptyList()
        val likes = mutableListOf<Long>()
        repeat(Random.nextInt(0, 1000)) {
            likes.add(it.toLong())
        }
        return likes
    }

    private fun generateSampleComments(): List<Post> {

        if (Random.nextBoolean()) return emptyList()

        val comments = mutableListOf<Post>()
        repeat(Random.nextInt(0, 10)) {
            comments.add(
                Post(
                    id = it,
                    userAccount = userAccounts.random(),
                    description = "Comentário $it",
                    date = getCurrentTime(),
                    medias = listOf(),
                    likes = listOf(),
                    comments = listOf()
                )
            )
        }
        return comments
    }
}
