package expo.modules.playgames

import android.app.Activity
import android.util.Log
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.PlayGamesSdk
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.kotlin.Promise

class ExpoPlayGamesModule : Module() {

  private val activity: Activity
    get() = appContext.activityProvider?.currentActivity
      ?: throw Exception("Activity is not available")

  override fun definition() = ModuleDefinition {
    Name("ExpoPlayGames")

    // -------------------------------------------------------------------------
    // Lifecycle — initialize the SDK as soon as the module is created
    // -------------------------------------------------------------------------
    OnCreate {
      val ctx = appContext.reactContext?.applicationContext
      Log.d("ExpoPlayGames", "OnCreate — applicationContext=${ctx != null}")
      ctx?.let { PlayGamesSdk.initialize(it) }
    }

    // -------------------------------------------------------------------------
    // Authentication
    // -------------------------------------------------------------------------

    AsyncFunction("signIn") { promise: Promise ->
      val client = PlayGames.getGamesSignInClient(activity)
      client.signIn()
        .addOnSuccessListener { result ->
          Log.d("ExpoPlayGames", "signIn result: isAuthenticated=${result.isAuthenticated}")
          promise.resolve(result.isAuthenticated)
        }
        .addOnFailureListener { e ->
          Log.e("ExpoPlayGames", "signIn failed: code=${(e as? com.google.android.gms.common.api.ApiException)?.statusCode} message=${e.message}")
          promise.reject("SIGN_IN_FAILED", e.message ?: "Sign in failed", e)
        }
    }

    AsyncFunction("signOut") { promise: Promise ->
      // Play Games v2 does not provide an explicit signOut — the user manages
      // account access from the Play Games app. We resolve immediately so the
      // JS side can treat the session as ended.
      promise.resolve(null)
    }

    AsyncFunction("isSignedIn") { promise: Promise ->
      val client = PlayGames.getGamesSignInClient(activity)
      client.isAuthenticated
        .addOnSuccessListener { result ->
          promise.resolve(result.isAuthenticated)
        }
        .addOnFailureListener { e ->
          promise.reject("IS_SIGNED_IN_FAILED", e.message ?: "Could not check sign-in state", e)
        }
    }

    AsyncFunction("getCurrentPlayer") { promise: Promise ->
      val client = PlayGames.getPlayersClient(activity)
      client.currentPlayer
        .addOnSuccessListener { player ->
          val map = mapOf(
            "playerId" to player.playerId,
            "displayName" to player.displayName,
            "title" to player.title
          )
          promise.resolve(map)
        }
        .addOnFailureListener { e ->
          promise.reject("GET_PLAYER_FAILED", e.message ?: "Could not retrieve player", e)
        }
    }

    // -------------------------------------------------------------------------
    // Leaderboards
    // -------------------------------------------------------------------------

    AsyncFunction("submitScore") { leaderboardId: String, score: Double, promise: Promise ->
      val client = PlayGames.getLeaderboardsClient(activity)
      client.submitScoreImmediate(leaderboardId, score.toLong())
        .addOnSuccessListener {
          promise.resolve(true)
        }
        .addOnFailureListener { e ->
          promise.reject("SUBMIT_SCORE_FAILED", e.message ?: "Score submission failed", e)
        }
    }

    AsyncFunction("showLeaderboard") { leaderboardId: String, promise: Promise ->
      val client = PlayGames.getLeaderboardsClient(activity)
      client.getLeaderboardIntent(leaderboardId)
        .addOnSuccessListener { intent ->
          activity.startActivityForResult(intent, 0)
          promise.resolve(null)
        }
        .addOnFailureListener { e ->
          promise.reject("SHOW_LEADERBOARD_FAILED", e.message ?: "Could not show leaderboard", e)
        }
    }

    AsyncFunction("showAllLeaderboards") { promise: Promise ->
      val client = PlayGames.getLeaderboardsClient(activity)
      client.allLeaderboardsIntent
        .addOnSuccessListener { intent ->
          Log.d("ExpoPlayGames", "showAllLeaderboards: got intent, starting activity")
          try {
            activity.startActivityForResult(intent, 0)
            Log.d("ExpoPlayGames", "showAllLeaderboards: startActivity called")
          } catch (e: Exception) {
            Log.e("ExpoPlayGames", "showAllLeaderboards: startActivity failed: ${e.message}")
          }
          promise.resolve(null)
        }
        .addOnFailureListener { e ->
          Log.e("ExpoPlayGames", "showAllLeaderboards failed: code=${(e as? com.google.android.gms.common.api.ApiException)?.statusCode} message=${e.message}")
          promise.reject("SHOW_ALL_LEADERBOARDS_FAILED", e.message ?: "Could not show leaderboards", e)
        }
    }

    // -------------------------------------------------------------------------
    // Achievements
    // -------------------------------------------------------------------------

    AsyncFunction("unlockAchievement") { achievementId: String, promise: Promise ->
      val client = PlayGames.getAchievementsClient(activity)
      client.unlockImmediate(achievementId)
        .addOnSuccessListener {
          promise.resolve(true)
        }
        .addOnFailureListener { e ->
          promise.reject("UNLOCK_ACHIEVEMENT_FAILED", e.message ?: "Achievement unlock failed", e)
        }
    }

    AsyncFunction("incrementAchievement") { achievementId: String, steps: Int, promise: Promise ->
      val client = PlayGames.getAchievementsClient(activity)
      client.incrementImmediate(achievementId, steps)
        .addOnSuccessListener { unlocked ->
          promise.resolve(unlocked)
        }
        .addOnFailureListener { e ->
          promise.reject("INCREMENT_ACHIEVEMENT_FAILED", e.message ?: "Achievement increment failed", e)
        }
    }

    AsyncFunction("showAchievements") { promise: Promise ->
      val client = PlayGames.getAchievementsClient(activity)
      client.achievementsIntent
        .addOnSuccessListener { intent ->
          activity.startActivityForResult(intent, 0)
          promise.resolve(null)
        }
        .addOnFailureListener { e ->
          promise.reject("SHOW_ACHIEVEMENTS_FAILED", e.message ?: "Could not show achievements", e)
        }
    }
  }
}
