package com.paradoxo.threadscompose.sampleData

import com.paradoxo.threadscompose.R
import com.paradoxo.threadscompose.model.Comment
import com.paradoxo.threadscompose.model.Notification
import com.paradoxo.threadscompose.model.NotificationTypeEnum
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
    private val images = listOf(
        "https://raw.githubusercontent.com/git-jr/sample-files/7bc859dfa8a6241fa9c0d723ba6e7517bdfedd50/profile%20pics/profile_pic_emoji_1.png",
        "https://raw.githubusercontent.com/git-jr/sample-files/7bc859dfa8a6241fa9c0d723ba6e7517bdfedd50/profile%20pics/profile_pic_emoji_2.png",
        "https://raw.githubusercontent.com/git-jr/sample-files/7bc859dfa8a6241fa9c0d723ba6e7517bdfedd50/profile%20pics/profile_pic_emoji_3.png",
        "https://raw.githubusercontent.com/git-jr/sample-files/7bc859dfa8a6241fa9c0d723ba6e7517bdfedd50/profile%20pics/profile_pic_emoji_4.png",
        "https://raw.githubusercontent.com/git-jr/sample-files/7bc859dfa8a6241fa9c0d723ba6e7517bdfedd50/profile%20pics/profile_pic_emoji_5.png",
    )

    init {
        val randomNumbers = mutableListOf<Long>()
        repeat(1000) {
            randomNumbers.add(it.toLong())
        }

        for (i in 1..10) {
            val userAccount = UserAccount(
                id = i.toString(),
                name = "Nome $i",
                userName = "usuario$i",
                bio = bios[i - 1],
                imageProfileUrl = images.random(),
                posts = randomNumbers.subList(0, Random.nextInt(1, 1000)),
                follows = randomNumbers.subList(0, Random.nextInt(1, 1000)),
                followers = randomNumbers.subList(0, Random.nextInt(1, 1000)),
            )
            userAccounts.add(userAccount)

            val post = Post(
                id = i.toString(),
                userAccount = userAccount,
                description = descriptions[i - 1],
                date = getCurrentTime(),
                medias = if (Random.nextBoolean()) images.shuffled() else listOf(),
                likes = generateListRandomIdStrings(),
                comments = generateSampleComments()
            )
            posts.add(post)
        }

        for (i in 1..10) {
            userAccounts[i - 1] =
                userAccounts[i - 1].copy(posts = posts.filter { it.userAccount.id == i.toString() }
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
                    type = if (it in 2..4) NotificationTypeEnum.values().sortedArray()[it] else
                        NotificationTypeEnum.values().sortedArray()[Random.nextInt(0, 5)],
                    isFollowing = Random.nextBoolean(),
                )
            )
        }

    }

    private fun generateListRandomIdStrings(): List<String> {
        if (Random.nextBoolean()) return emptyList()
        val randomList = mutableListOf<String>()
        repeat(Random.nextInt(0, 1000)) {
            randomList.add(UUID.randomUUID().toString())
        }
        return randomList
    }

    private fun generateSampleComments(): List<Comment> {

        if (Random.nextBoolean()) return emptyList()

        val comments = mutableListOf<Comment>()
        repeat(Random.nextInt(0, 10)) {
            comments.add(
                Comment(
                    id = it.toString(),
                    profilePicAuthor = images.random(),
                )
            )
        }
        return comments
    }

    fun generateSampleInvitedUser(): UserAccount {
        return UserAccount(
            id = UUID.randomUUID().toString(),
            name = "Nome da pessoa convidada",
            userName = "convidada_42",
            bio = "O tal do Lorem ipsum dolor sit amet, consectetur adipiscing elit",
            link = "https://github.com/git-jr",
            imageProfileUrl = images.random(),
            posts = (1L..Random.nextLong(100)).toList(),
            follows = (1L..Random.nextLong(100)).toList(),
            followers = (1L..Random.nextLong(100)).toList(),
        )
    }
}
