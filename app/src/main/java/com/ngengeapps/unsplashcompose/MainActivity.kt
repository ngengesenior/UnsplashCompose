package com.ngengeapps.unsplashcompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.ngengeapps.unsplashcompose.models.Photo
import com.ngengeapps.unsplashcompose.ui.theme.UnsplashComposeTheme
import com.ngengeapps.unsplashcompose.utils.Constants
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.imageloading.ImageLoadState.Loading
import com.ngengeapps.unsplashcompose.models.User
import java.util.*

@ExperimentalPagerApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnsplashComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    AppScreen()
                }
            }
        }
    }
}



@ExperimentalPagerApi
@Composable
fun AppScreen() {
    val state = rememberScaffoldState()
    Scaffold(
        scaffoldState = state,
        topBar = {
            TopAppBar(backgroundColor = MaterialTheme.colors.surface) {
                Icon(painterResource(id = R.drawable.unsplash_symbol),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp) )
                Spacer(modifier = Modifier.width(6.dp))
                SearchView()
                Spacer(modifier = Modifier.width(6.dp))

                Surface(
                    shape = CircleShape,
                    color = Color.LightGray

                ) {
                    Icon(Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(20.dp)) )
                }

                Spacer(modifier = Modifier.width(4.dp))
                MainPopup()
                /*Icon(Icons.Default.List,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp) )*/

                
            }
        
    }){
        PagerView()
    }
}

@Composable
fun SearchView() {
    TextField(
        value = "",onValueChange = {},leadingIcon = {
        Icon(Icons.Rounded.Search, contentDescription = null )
    },
        placeholder = {
            Text(text = "Search")
        },
        modifier = Modifier
            .border(
                border = BorderStroke(2.dp, Color.Black),
                shape = RoundedCornerShape(20.dp)

            )
            .height(50.dp)
    )
}

@ExperimentalPagerApi
@Preview
@Composable
fun PreviewAppScreen() {
    UnsplashComposeTheme {
        AppScreen()
    }

}




@ExperimentalPagerApi
@Composable
fun PagerView() {
    val pagerState = rememberPagerState(pageCount = Constants.pages.size)
    val scope = rememberCoroutineScope()
    val pages by rememberSaveable{ mutableStateOf(Constants.pages)}

    Column {
        ScrollableTabRow(selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState,tabPositions))
            },
            backgroundColor = MaterialTheme.colors.surface
        ) {
            pages.forEachIndexed { index, topic ->
                Tab(
                    text = {Text(text = topic.replace("-"," ").capitalize(Locale.US))},
                    selected = pagerState.currentPage == index
                    , onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(page = index)
                        }
                    })
            }

        }
        HorizontalPager(state = pagerState) { page ->
            TopicPageView(topic = pages[page])
        }
    }

}




@Composable
fun PhotoItem(photo: Photo) {
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .clickable {
            Toast
                .makeText(context, "Photo by ${photo.user.username} ", Toast.LENGTH_SHORT)
                .show()

        }){

        UserRow(user = photo.user)

        Spacer(modifier = Modifier.height(10.dp))

        MainPhotoView(url = photo.urls.full)

        Spacer(modifier = Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            Spacer(modifier = Modifier.width(6.dp))
            IconElevated(imageVector = Icons.Rounded.Favorite)
            Spacer(modifier = Modifier.width(4.dp))
            IconElevated(imageVector = Icons.Rounded.Add)
            Spacer(modifier = Modifier.weight(1f))
            DownloadButton()
            IconElevated(imageVector = Icons.Rounded.KeyboardArrowDown)
            Spacer(modifier = Modifier.width(6.dp))


        }

        //Spacer(modifier = Modifier.height(16.dp))



    }

}




@Composable
fun UserRow(user: User) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Image(painter = rememberCoilPainter(
            request = user.profile_image.medium),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(
                    RoundedCornerShape(20.dp)
                ))
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = user.name,
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun TopicPageView(topic:String) {
    val lazyPagingItems = Constants.getUnsplashPageFlow(topic).collectAsLazyPagingItems()
    LazyColumn{
        items(lazyPagingItems) { photo ->
            photo?.let {
                PhotoItem(photo = it)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }


    }
}


@Composable
fun IconElevated(imageVector: ImageVector) {
    Surface(contentColor = MaterialTheme.colors.onSurface,
        shape = RectangleShape,
        elevation = 8.dp,
        border = BorderStroke(0.5.dp,Color.Gray)
    ) {
        Icon(imageVector = imageVector,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.padding(3.dp) )

    }
}


@Composable
fun MainPhotoView(url:String){
    val painter = rememberCoilPainter(request = url,fadeIn = true)

    Box(Modifier.fillMaxWidth()) {
        Image(painter = painter,
            contentDescription = null)

        when(painter.loadState){
            is Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            is ImageLoadState.Error -> {

            }
            else -> {
                
            }
        }
    }

}

@Composable
fun MainPopup() {
    val expanded = remember { mutableStateOf(false)}
    val context = LocalContext.current
    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize()

    ) {

        IconButton(onClick = {
            expanded.value = !expanded.value
        }) {
            Icon(Icons.Rounded.Menu, contentDescription = null )
        }
        
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false },
            modifier = Modifier.background(MaterialTheme.colors.onSurface)) {
            Constants.mainPopUp.forEach {
                DropdownMenuItem(onClick = {
                    Toast.makeText(context,"You clicked on $it",Toast.LENGTH_SHORT).show()
                }) {
                    Text(text = it,color = MaterialTheme.colors.surface)
                }
            }
            Row{

                Spacer(modifier = Modifier.width(6.dp))
                DropDownIcon(resource = R.drawable.ic_twitter)
                Spacer(modifier = Modifier.width(6.dp))
                DropDownIcon(resource = R.drawable.ic_facebook)
                Spacer(modifier = Modifier.width(6.dp))
                DropDownIcon(resource = R.drawable.ic_instagram)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colors.surface)
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Help",color = MaterialTheme.colors.surface)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = ".",color = MaterialTheme.colors.surface)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Licenses",color = MaterialTheme.colors.surface)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = ".",color = MaterialTheme.colors.surface)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Press",color = MaterialTheme.colors.surface)

            }

        }

    }

}


@Composable
fun DropDownIcon(@DrawableRes resource:Int) {
    Surface(modifier = Modifier.size(20.dp),shape = CircleShape) {
        Icon(painterResource(id = resource), contentDescription =null )
    }
}

@Composable
fun DownloadButton() {
    Surface(elevation = 8.dp,
    border = BorderStroke(1.dp,Color.LightGray)) {
        Text(text = "Download",
        modifier = Modifier.padding(horizontal = 4.dp,vertical = 4.dp))
    }
}

