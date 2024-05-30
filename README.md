# Djoser, Retrofit, Java

How to perform user registration with django, djoser, android, retrofit, gson, internet, same device.

## How to integrate django authentication with android java retrofit

To integrate Django authentication with an Android app using Retrofit, you'll need to set up both the Django backend and the Android frontend to handle the authentication process. Here’s a step-by-step guide:

### 1. Set Up Django Backend for Authentication

#### a. Install Django and Django REST framework

```bash
pip install django djangorestframework djangorestframework-simplejwt
```

#### b. Configure Django Settings

Add the required apps to your `INSTALLED_APPS` in `settings.py`:

```python
INSTALLED_APPS = [
    ...
    'rest_framework',
    'rest_framework_simplejwt',
    ...
]
```

Configure Django REST framework to use JWT authentication:

```python
REST_FRAMEWORK = {
    'DEFAULT_AUTHENTICATION_CLASSES': (
        'rest_framework_simplejwt.authentication.JWTAuthentication',
    ),
}
```

Add URL patterns for the JWT authentication views in `urls.py`:

```python
from django.urls import path
from rest_framework_simplejwt.views import (
    TokenObtainPairView,
    TokenRefreshView,
)

urlpatterns = [
    ...
    path('api/token/', TokenObtainPairView.as_view(), name='token_obtain_pair'),
    path('api/token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),
]
```

#### c. Create a View for Protected Resources

Create a simple view that requires authentication:

```python
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response

@api_view(['GET'])
@permission_classes([IsAuthenticated])
def protected_view(request):
    return Response({'message': 'This is a protected view!'})
```

Add this view to your `urls.py`:

```python
urlpatterns += [
    path('api/protected/', protected_view, name='protected'),
]
```

### 2. Set Up Android App with Retrofit

#### a. Add Dependencies

In your `build.gradle` file, add Retrofit and OkHttp dependencies:

```gradle
dependencies {
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:okhttp:4.9.1'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.9.1'
}
```

#### b. Create Retrofit Instance

Create a singleton class for Retrofit instance:

```java
public class RetrofitClient {
    private static RetrofitClient instance = null;
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://your-django-api-url/";

    private RetrofitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }
}
```

#### c. Define API Interface

Create an interface for API endpoints:

```java
public interface Api {
    @POST("api/token/")
    Call<TokenResponse> getToken(@Body TokenRequest tokenRequest);

    @GET("api/protected/")
    Call<ProtectedResponse> getProtectedResource(@Header("Authorization") String token);
}
```

Define `TokenRequest`, `TokenResponse`, and `ProtectedResponse` models:

```java
public class TokenRequest {
    private String username;
    private String password;

    public TokenRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

public class TokenResponse {
    private String access;
    private String refresh;

    // getters and setters
}

public class ProtectedResponse {
    private String message;

    // getters and setters
}
```

#### d. Make API Calls

In your Activity or ViewModel, use Retrofit to make API calls:

```java
RetrofitClient.getInstance().getApi().getToken(new TokenRequest("username", "password"))
        .enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String accessToken = "Bearer " + response.body().getAccess();

                    RetrofitClient.getInstance().getApi().getProtectedResource(accessToken)
                            .enqueue(new Callback<ProtectedResponse>() {
                                @Override
                                public void onResponse(Call<ProtectedResponse> call, Response<ProtectedResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        // Handle the protected resource response
                                    }
                                }

                                @Override
                                public void onFailure(Call<ProtectedResponse> call, Throwable t) {
                                    // Handle failure
                                }
                            });
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                // Handle failure
            }
        });
```

### Summary

This setup allows your Android app to authenticate with a Django backend using JWT tokens. The user logs in, receives a JWT token, and uses it to access protected resources.