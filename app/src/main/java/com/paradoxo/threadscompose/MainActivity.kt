package com.paradoxo.threadscompose

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.facebook.Profile
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.paradoxo.threadscompose.model.Post
import com.paradoxo.threadscompose.model.UserAccount
import com.paradoxo.threadscompose.network.firebase.PostFirestore
import com.paradoxo.threadscompose.sampleData.SampleData
import com.paradoxo.threadscompose.ui.FeedScreen
import com.paradoxo.threadscompose.ui.NotificationsScreen
import com.paradoxo.threadscompose.ui.PostScreen
import com.paradoxo.threadscompose.ui.SearchScreen
import com.paradoxo.threadscompose.ui.login.AppState
import com.paradoxo.threadscompose.ui.login.LoginScreen
import com.paradoxo.threadscompose.ui.login.LoginViewModel
import com.paradoxo.threadscompose.ui.profile.ProfileEditScreen
import com.paradoxo.threadscompose.ui.profile.ProfileScreen
import com.paradoxo.threadscompose.ui.theme.ThreadsComposeTheme
import com.paradoxo.threadscompose.utils.showMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val testMode = false

        if (testMode) {
            setContent {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LottieStyles()
                }
            }
        } else {
            setContent {
                ThreadsComposeTheme {
                    Box(
                        Modifier.fillMaxSize()
                    ) {
                        val navController: NavHostController = rememberNavController()

                        var showNavigationBar by remember { mutableStateOf(false) }
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination

                        val destinysWithoutNavigationBar by remember {
                            mutableStateOf(
                                listOf(
                                    Destiny.Login.route,
                                    Destiny.ProfileEdit.route,
                                    Destiny.Post.route,
                                )
                            )
                        }

                        LaunchedEffect(currentDestination) {
                            showNavigationBar =
                                !destinysWithoutNavigationBar.contains(currentDestination?.route)
                        }

                        ThreadsApp(
                            navController = navController,
                            showNavigationBar = showNavigationBar,
                            currentDestination = currentDestination,
                            content = {
                                ThreadsNavHost(
                                    navController = navController,
                                    navigateToInstagram = {
                                        val instagramIntent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://www.instagram.com/threadsapp/")
                                        )
                                        startActivity(instagramIntent)
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun LottieStyles() {
        val url =
            "https://lottie.host/a2a247fa-25e6-4884-b30c-47af6bb0ce31/3Bz9KEEqBj.json"

        val sampleTest = 3
        when (sampleTest) {
            1 -> {
                val composition by rememberLottieComposition(
                    spec = LottieCompositionSpec.Url(
                        url
                    )
                )

                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )
            }

            2 -> {
                val anim = rememberLottieAnimatable()
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.Url(url)
                )
                var sliderGestureProgress: Float? by remember { mutableStateOf(null) }
                LaunchedEffect(composition, sliderGestureProgress) {
                    when (val p = sliderGestureProgress) {
                        null -> anim.animate(
                            composition,
                            iterations = 0,
                            initialProgress = anim.progress,
                            continueFromPreviousAnimate = false,
                        )

                        else -> anim.snapTo(progress = p)
                    }
                }
                Box(Modifier.padding(bottom = 32.dp)) {
                    LottieAnimation(anim.composition, { anim.progress })
                    Slider(
                        value = sliderGestureProgress ?: anim.progress,
                        onValueChange = { sliderGestureProgress = it },
                        onValueChangeFinished = { sliderGestureProgress = null },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                    )
                }
            }

            3 -> {
                var previousProgress by remember { mutableFloatStateOf(0f) }
                var speed by remember { mutableFloatStateOf(1f) }
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(
                        R.raw.logo_lines_animated
                    )
                )
                val animatable = rememberLottieAnimatable()

                val context = LocalContext.current
                context.showMessage(animatable.isPlaying.toString())

                LaunchedEffect(speed) {
                    animatable.animate(
                        composition,
                        iteration = LottieConstants.IterateForever,
                        speed = speed,
                        initialProgress = previousProgress,
                    )
                }
                val interactionSource = remember { MutableInteractionSource() }

                LottieAnimation(
                    composition = composition,
                    progress = { animatable.progress },
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            speed = if (speed == 0f) 1f else 0f
                            previousProgress = animatable.progress
                        }
                )
            }

            4 -> {
                var previsousProgress by remember { mutableStateOf(0f) }
                var velocity by remember { mutableStateOf(0f) }
                var shouldPlay by remember { mutableStateOf(true) }
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.Url(
                        url
                    )
                )
                val animatable = rememberLottieAnimatable()

                //                            LaunchedEffect(composition, shouldPlay) {
                //                                if (composition == null || !shouldPlay) return@LaunchedEffect
                //                                animatable.animate(
                //                                    composition,
                //                                    iteration = LottieConstants.IterateForever,
                //                                )
                //                            }

                LaunchedEffect(shouldPlay) {
                    animatable.animate(
                        composition,
                        iteration = LottieConstants.IterateForever,
                        speed = velocity,
                        initialProgress = previsousProgress,
                    )
                }
                val interactionSource = remember { MutableInteractionSource() }


                LottieAnimation(
                    composition = composition,
                    progress = { animatable.progress },
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            shouldPlay = !shouldPlay
                            velocity = if (velocity == 0f) 1f else 0f
                            previsousProgress = animatable.progress
                        }
                )

                Text(
                    text = "Progresso: ${animatable.progress}",
                    Modifier.offset(y = (-300).dp)
                )
            }

            5 -> {
                val anim = rememberLottieAnimatable()
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.Url(
                        url
                    )
                )
                var speed by remember { mutableStateOf(1f) }
                LaunchedEffect(composition, speed) {
                    anim.animate(
                        composition,
                        iterations = LottieConstants.IterateForever,
                        speed = speed,
                        initialProgress = anim.progress,
                    )
                }
                Column(
                    Modifier.navigationBarsPadding()
                ) {
                    Box {
                        LottieAnimation(composition, { anim.progress })
                        Slider(
                            value = speed,
                            onValueChange = { speed = it },
                            valueRange = -3f..3f,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 24.dp)
                                .size(width = 1.dp, height = 16.dp)
                                .background(Color.Black)
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }


            }
        }
    }

    @Composable
    fun ThreadsApp(
        content: @Composable (PaddingValues) -> Unit,
        navController: NavHostController,
        showNavigationBar: Boolean,
        currentDestination: NavDestination?
    ) {
        Scaffold(
            bottomBar = {
                if (showNavigationBar) {
                    NavigationBar(
                        containerColor = Color.White,
                    ) {
                        screenItems.forEach { screen ->
                            val isSelected =
                                currentDestination?.hierarchy?.any { it.route == screen.route } == true

                            NavigationBarItem(
                                icon = {
                                    screen.resourceId?.let { assetIcon ->
                                        val icon = painterResource(
                                            id = if (isSelected) {
                                                assetIcon.first
                                            } else {
                                                assetIcon.second
                                            },
                                        )


                                        Icon(
                                            icon,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .alpha(
                                                    if (isSelected) 1f else 0.5f
                                                )
                                        )
                                    }
                                },
                                selected = isSelected,
                                onClick = {
                                    if (screen.route == Destiny.Post.route) {
                                        navController.navigate(screen.route) {
                                            launchSingleTop = true
                                        }
                                    } else {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }

                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(Modifier.fillMaxSize()) {
                    content(paddingValues)
                }
            }
        }
    }

    @Composable
    fun ThreadsNavHost(
        navController: NavHostController,
        navigateToInstagram: () -> Unit = {}
    ) {
        val sessionViewModel: SessionViewModel = viewModel()
        val state = sessionViewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = Destiny.LoginNavigation.route
        ) {
            loginGraph(
                onNavigateToHome = { currentUser ->
                    sessionViewModel.serCurrent(currentUser)

                    navController.navigate(Destiny.HomeNavigation.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToProfileEdit = {
                    navController.navigate(Destiny.ProfileEdit.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            homeGraph(
                state = state,
                onNavigateToInstagram = {
                    navigateToInstagram()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }

    private fun NavGraphBuilder.loginGraph(
        onNavigateToHome: (UserAccount) -> Unit = {},
        onNavigateToProfileEdit: () -> Unit = {}
    ) {
        navigation(
            startDestination = Destiny.Login.route,
            route = Destiny.LoginNavigation.route
        ) {
            composable(Destiny.Login.route) {
                val loginViewModel: LoginViewModel = viewModel()

                LoginScreen(
                    loginViewModel = loginViewModel,
                    onAuthComplete = {
                        it?.let {
                            loginViewModel.getProfile(
                                onSuccess = { userAccountOnFirebase ->
                                    if (userAccountOnFirebase.userName.isNotEmpty()) {
                                        onNavigateToHome(userAccountOnFirebase)
                                    } else {
                                        onNavigateToProfileEdit()
                                    }
                                },
                                onError = {
                                    onNavigateToProfileEdit()
                                }
                            )
                        } ?: run {
                            onNavigateToHome(SampleData().generateSampleInvitedUser())
                        }
                    },
                )
            }

            composable(Destiny.ProfileEdit.route) {
                val loginViewModel: LoginViewModel = viewModel()
                val context = LocalContext.current

                val currentUser = Firebase.auth.currentUser

                val profile = Profile.getCurrentProfile()
                val profilePicUri = profile?.getProfilePictureUri(200, 200)

                val userAccount = UserAccount(
                    id = currentUser?.uid ?: "",
                    name = currentUser?.displayName ?: "",
                    userName = currentUser?.displayName?.lowercase()?.replace(" ", "_") ?: "",
                    imageProfileUrl = profilePicUri?.toString() ?: "",
                )

                ProfileEditScreen(
                    userAccount = userAccount,
                    onSave = { userAccountUpdated ->
                        loginViewModel.saveNewUser(
                            userAccount = userAccountUpdated,
                            onSuccess = {
                                onNavigateToHome(userAccountUpdated)
                            },
                            onError = {
                                context.showMessage("Erro ao salvar informações do perfil")
                            }
                        )
                    }
                )
            }
        }
    }

    private fun NavGraphBuilder.homeGraph(
        state: State<SessionState>,
        onNavigateToInstagram: () -> Unit = {},
        onBack: () -> Unit = {},
        paddingValues: PaddingValues = PaddingValues(0.dp)
    ) {
        navigation(
            startDestination = Destiny.Feed.route,
            route = Destiny.HomeNavigation.route
        ) {
            composable(Destiny.Feed.route) {
                val postViewModel: FeedViewModel = viewModel()
                val postState by postViewModel.uiState.collectAsState()

                FeedScreen(posts = postState.posts)
            }
            composable(Destiny.Search.route) { SearchScreen() }
            composable(Destiny.Post.route) {

                val scope = rememberCoroutineScope()

                PostScreen(
                    currentUser = state.value.userAccount,
                    onBack = {
                        onBack()
                    },
                    onSendPost = { posts ->
                        val postFirestore = PostFirestore()
                        scope.launch {
                            postFirestore.insertPost(
                                posts = posts,
                                onSuccess = {
                                    onBack()
                                },
                                onError = {
                                    onBack()
                                }
                            )
                        }

                    }
                )
            }
            composable(Destiny.Notifications.route) { NotificationsScreen() }
            composable(Destiny.Profile.route) {
                ProfileScreen(
                    currentUser = state.value.userAccount,
                    modifier = Modifier.padding(paddingValues),
                    onNavigateToInstagram = {
                        onNavigateToInstagram()
                    }
                )
            }
        }
    }

    sealed class Destiny(val route: String, val resourceId: Pair<Int, Int>? = null) {
        object Login : Destiny("login")
        object Feed : Destiny("feed", Pair(R.drawable.ic_home, R.drawable.ic_home_outlined))
        object Search : Destiny("search", Pair(R.drawable.ic_search, R.drawable.ic_search))
        object Post : Destiny("post", Pair(R.drawable.ic_post, R.drawable.ic_post))
        object Notifications :
            Destiny("notifications", Pair(R.drawable.ic_heart, R.drawable.ic_heart_outlined))

        object Profile :
            Destiny("profile", Pair(R.drawable.ic_profile, R.drawable.ic_profile_outlined))

        object ProfileEdit : Destiny("profileEdit")

        object LoginNavigation : Destiny("loginNavigation")
        object HomeNavigation : Destiny("homeNavigation")
    }

    private val screenItems = listOf(
        Destiny.Feed,
        Destiny.Search,
        Destiny.Post,
        Destiny.Notifications,
        Destiny.Profile
    )
}

internal class FeedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FeedScreenState())
    val uiState: StateFlow<FeedScreenState> = _uiState.asStateFlow()

    init {
        val postFirestore = PostFirestore()
        postFirestore.getAllPosts(
            onSuccess = { posts ->
                _uiState.value = _uiState.value.copy(posts = posts)
            },
            onError = {
                _uiState.value = _uiState.value.copy(posts = SampleData().posts)
            }
        )
    }
}

data class FeedScreenState(
    var posts: List<Post> = emptyList(),
)

internal class SessionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SessionState())
    val uiState: StateFlow<SessionState> = _uiState.asStateFlow()

    fun serCurrent(currentUser: UserAccount) {
        _uiState.value = _uiState.value.copy(userAccount = currentUser)
    }
}

data class SessionState(
    var appState: AppState = AppState.Loading,
    var userAccount: UserAccount = UserAccount(),
)

enum class DragAnchors(val fraction: Float) {
    Start(.050f),
    End(.5f),
}

@Composable
private fun DraggableContent(
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = modifier)
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun VerticalDraggableSampleTest(
    modifier: Modifier = Modifier,
) {
    val minHeightImage = 32.dp
    var imageSize by remember { mutableStateOf(0.dp) }
    var currentPosition by remember { mutableStateOf(0.dp) }

    val density = LocalDensity.current
    val positionalThreshold = { distance: Float -> distance * 0.5f }
    val velocityThreshold = { with(density) { 100.dp.toPx() } }
    val animationSpec = tween<Float>()
    val state = rememberSaveable(
        density,
        saver = AnchoredDraggableState.Saver(
            animationSpec = animationSpec,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
        )
    ) {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
            animationSpec = animationSpec,
        )
    }
//    val contentSize = 80.dp
//    val contentSizePx = with(density) { contentSize.toPx() }


    // Quando o arrastar chegar no final, volta pa o inicio com uma animação
    if (state.currentValue == DragAnchors.End) {
        LaunchedEffect(Unit) {
            state.animateTo(DragAnchors.Start)
        }
    }

    Box(
        modifier
            .onSizeChanged { layoutSize ->
                val dragEndPoint = layoutSize.height
                state.updateAnchors(
                    DraggableAnchors {
                        DragAnchors
                            .values()
                            .forEach { anchor ->
                                anchor at dragEndPoint * anchor.fraction
                            }
                    }
                )
            }
    ) {
        DraggableContent(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    val intOffset = IntOffset(
                        x = 0,
                        y = state
                            .requireOffset()
                            .roundToInt(),
                    )
                    currentPosition = if (intOffset.y.dp != 0.dp) (intOffset.y.dp / 8) else 0.dp
                    imageSize =
                        if (currentPosition < minHeightImage) minHeightImage else currentPosition
                    intOffset
                }
                .anchoredDraggable(state, Orientation.Vertical),
        )
    }

    Column(
        Modifier.fillMaxHeight(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo_colors),
            contentDescription = null,
            modifier = Modifier
                .size(imageSize)
                .align(Alignment.CenterHorizontally)
        )

    }
}


@Composable
private fun VerticalDraggableSampleTest1(
    modifier: Modifier = Modifier
) {
    val maxHeightImage = 200.dp
    val defaultSizeImage = 72.dp
    val coroutineScope = rememberCoroutineScope()
    val animatedImageSize = remember { Animatable(defaultSizeImage.value) }

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Box(
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { change, offset ->
                                coroutineScope.launch {
                                    val newSize =
                                        (animatedImageSize.value + offset / 8).coerceAtLeast(
                                            defaultSizeImage.value
                                        )
                                    if (newSize < maxHeightImage.value) {
                                        animatedImageSize.snapTo(newSize)
                                    }
                                    change.consume()
                                }
                            },
                            onDragEnd = {
                                coroutineScope.launch {
                                    animatedImageSize.animateTo(
                                        defaultSizeImage.value,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioLowBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                }
                            }
                        )
                    }
                    .onGloballyPositioned {
//                        if (it.positionInParent().y == 0f) { // Check if the top of the box is visible
//                            // Animate back to default size
//                            coroutineScope.launch {
//                                animatedImageSize.animateTo(
//                                    defaultSizeImage.value,
//                                    animationSpec = spring(
//                                        dampingRatio = Spring.DampingRatioMediumBouncy,
//                                        stiffness = Spring.StiffnessLow
//                                    )
//                                )
//                            }
//                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_logo_colors),
                        contentDescription = null,
                        modifier = Modifier
                            .size(animatedImageSize.value.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }

        items(100) { index ->
            Text(text = "Item $index")
        }
    }
}

@Composable
private fun VerticalDraggableSampleTest2(
    modifier: Modifier = Modifier,
) {
    val defaultSizeImage = 72.dp // Define o tamanho específico que a imagem deve retornar

    var imageSize by remember { mutableStateOf(defaultSizeImage) }
    val coroutineScope = rememberCoroutineScope()

    val draggableState = remember {
        mutableStateOf(imageSize)
    }
    val animatedOffsetY = remember {
        Animatable(0f)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    coroutineScope.launch {
                        // Scaling imageSize based on zoom
                        draggableState.value =
                            (draggableState.value * zoom).coerceAtLeast(defaultSizeImage)
                    }
                }
            }
            .verticalScroll(rememberScrollState())
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_colors),
                contentDescription = null,
                modifier = Modifier
                    .size(imageSize)
                    .fillMaxWidth()
//                    .align(Alignment.CenterHorizontally)
            )
        }
        items(100) { index ->
// Your list items
            Text(text = "Item $index")
        }
    }

// Handling drag gestures
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        imageSize = defaultSizeImage
                        // Animação para retornar a imagem ao tamanho desejado quando soltar
//                        coroutineScope.launch {
//                            animatedOffsetY.animateTo(
//                                targetValue = 0f,
//                                animationSpec = spring(
//                                    dampingRatio = Spring.DampingRatioMediumBouncy, // Amortecimento
//                                    stiffness = Spring.StiffnessLow // Rigidez
//                                )
//                            )
//                            imageSize = defaultSizeImage
//                        }
                    },
                    onVerticalDrag = { change, offset ->
                        coroutineScope.launch {
                            val newSize =
                                (imageSize + (offset / 8).dp).coerceAtLeast(defaultSizeImage)
                            animatedOffsetY.snapTo(offset)
                            imageSize = newSize
                            change.consumeAllChanges()
                        }
                    }
                )
            }
            .onGloballyPositioned { layoutCoordinates ->
                val layoutHeight = layoutCoordinates.size.height.toFloat()
                val dragHeight = animatedOffsetY.value

                // Scaling imageSize based on drag height
                imageSize =
                    (defaultSizeImage + (dragHeight / 8).dp).coerceIn(
                        defaultSizeImage,
                        layoutHeight.dp
                    )
            }
    ) {
        DraggableContent(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(0, animatedOffsetY.value.roundToInt()) }
        )
    }
}

@Composable
private fun VerticalDraggableSampleTest3(
    modifier: Modifier = Modifier,
) {
    val defaultSizeImage = 72.dp // Define o tamanho específico que a imagem deve retornar

    var imageSize by remember { mutableStateOf(defaultSizeImage) }
    val coroutineScope = rememberCoroutineScope()

    val draggableState = remember {
        mutableStateOf(imageSize)
    }
    val animatedOffsetY = remember {
        Animatable(0f)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    coroutineScope.launch {
                        // Scaling imageSize based on zoom
                        draggableState.value =
                            (draggableState.value * zoom).coerceAtLeast(defaultSizeImage)
                    }
                }
            }
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_colors),
                contentDescription = null,
                modifier = Modifier
                    .size(imageSize)
                    .fillMaxWidth()
//                    .align(Alignment.CenterHorizontally)
            )
        }
        items(100) { index ->
            // Your list items
            Text(text = "Item $index")
        }
    }

    // Handling drag gestures
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        imageSize = defaultSizeImage
                        // Animação para retornar a imagem ao tamanho desejado quando soltar
//                        coroutineScope.launch {
//                            animatedOffsetY.animateTo(
//                                targetValue = 0f,
//                                animationSpec = spring(
//                                    dampingRatio = Spring.DampingRatioMediumBouncy, // Amortecimento
//                                    stiffness = Spring.StiffnessLow // Rigidez
//                                )
//                            )
//                            imageSize = defaultSizeImage
//                        }
                    },
                    onVerticalDrag = { change, offset ->
                        coroutineScope.launch {
                            val newSize =
                                (imageSize + (offset / 8).dp).coerceAtLeast(defaultSizeImage)
                            animatedOffsetY.snapTo(offset)
                            imageSize = newSize
                            change.consumeAllChanges()
                        }
                    }
                )
            }
            .onGloballyPositioned { layoutCoordinates ->
                val layoutHeight = layoutCoordinates.size.height.toFloat()
                val dragHeight = animatedOffsetY.value

                // Scaling imageSize based on drag height
                imageSize =
                    (defaultSizeImage + (dragHeight / 8).dp).coerceIn(
                        defaultSizeImage,
                        layoutHeight.dp
                    )
            }
    ) {
        DraggableContent(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(0, animatedOffsetY.value.roundToInt()) }
        )
    }
}


