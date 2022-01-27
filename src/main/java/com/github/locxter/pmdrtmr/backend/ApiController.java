// Setting the package
package com.github.locxter.pmdrtmr.backend;

// Including needed classes/interfaces
import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.component.VTodo;
import biweekly.util.DateTimeComponents;
import biweekly.util.ICalDate;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// ApiController class
@RestController
public class ApiController
{
    // Attributes
    @Autowired
    private TimerRepository timerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;
    @Autowired
    private ConsumerTokenServices consumerTokenServices;
    private final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private final String SHORT_BREAK_DESCRIPTION = "Take some time to relax and enjoy life";
    private final String LONG_BREAK_DESCRIPTION = "Take some longer time to relax and enjoy life";
    
    // Function to check whether a user has valid credentials
    private boolean userHasCredentials(User user)
    {
        return user.getUsername() != null && !user.getUsername().isEmpty() && user.getPassword() != null && !user.getPassword().isEmpty();
    }

    // Function to check whether a user has valid settings
    private boolean userHasSettings(User user)
    {
        return user.getWorkDuration() > 0 && user.getShortBreakDuration() > 0 && user.getLongBreakDuration() > 0 && user.getLongBreakRatio() > 0 && user.getCaldavAddress() != null;
    }

    // Function to check whether a timer has a valid description
    private boolean timerHasDescription(Timer timer)
    {
        return timer.getDescription() != null && !timer.getDescription().isEmpty();
    }

    // Function to update timer durations (work, short break and long break) after settings were changed or a timer was deleted
    private void updateTimerDurations(User user)
    {
        if (user != null)
        {
            List<Timer> timers = timerRepository.findByUserId(user.getId());
            for (int i = 0; i < timers.size(); i++)
            {
                Timer timer = timers.get(i);
                if (timer.getIsBreak())
                {
                    if ((i + 1) % (user.getLongBreakRatio() * 2) == 0)
                    {
                        timer.setDescription(LONG_BREAK_DESCRIPTION);
                        timer.setDuration(user.getLongBreakDuration());
                    }
                    else
                    {
                        timer.setDescription(SHORT_BREAK_DESCRIPTION);
                        timer.setDuration(user.getShortBreakDuration());
                    }
                }
                else
                {
                    timer.setDuration(user.getWorkDuration());
                }
                timerRepository.save(timer);
            }
        }
    }

    // Function for revoking an access token
    @DeleteMapping("/oauth/revoke-token")
    public ResponseEntity revokeAccessToken(Authentication authentication)
    {
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        OAuth2AccessToken accessToken = authorizationServerTokenServices.getAccessToken(oAuth2Authentication);
        consumerTokenServices.revokeToken(accessToken.getValue());
        return new ResponseEntity(HttpStatus.OK);
    }

    // Function for signing up a new user
    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody User user)
    {
        if (userHasCredentials(user) && !userRepository.existsByUsername(user.getUsername()))
        {
            user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
            user.setWorkDuration(25);
            user.setShortBreakDuration(5);
            user.setLongBreakDuration(25);
            user.setLongBreakRatio(4);
            user.setCaldavAddress("");
            return new ResponseEntity(userRepository.save(user), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity("User exists or invalid credentials", HttpStatus.NOT_FOUND);
        }
    }

    // Function for retrieving an user
    @GetMapping("/user")
    public ResponseEntity getUser(Authentication authentication)
    {
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        if (user != null)
        {
            return new ResponseEntity(user, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
    }

    // Function for updating an user
    @PutMapping("/user")
    public ResponseEntity updateUser(Authentication authentication, @RequestBody User updatedUser)
    {
        if (userHasCredentials(updatedUser) && userHasSettings(updatedUser))
        {
            User user = userRepository.findByUsername(authentication.getName()).orElse(null);
            if (user != null && (user.getUsername().equals(updatedUser.getUsername()) || !userRepository.existsByUsername(updatedUser.getUsername())))
            {
                user.setUsername(updatedUser.getUsername());
                user.setPassword(PASSWORD_ENCODER.encode(updatedUser.getPassword()));
                user.setWorkDuration(updatedUser.getWorkDuration());
                user.setShortBreakDuration(updatedUser.getShortBreakDuration());
                user.setLongBreakDuration(updatedUser.getLongBreakDuration());
                user.setLongBreakRatio(updatedUser.getLongBreakRatio());
                user.setCaldavAddress(updatedUser.getCaldavAddress());
                userRepository.save(user);
                updateTimerDurations(user);
                return new ResponseEntity(user, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity("User not found or invalid update", HttpStatus.NOT_FOUND);
            }
        }
        else
        {
            return new ResponseEntity("User not found or invalid update", HttpStatus.NOT_FOUND);
        }
    }

    // Function for deleting an user
    @DeleteMapping("/user")
    public ResponseEntity deleteUser(Authentication authentication)
    {
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        if (user != null)
        {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
            OAuth2AccessToken accessToken = authorizationServerTokenServices.getAccessToken(oAuth2Authentication);
            consumerTokenServices.revokeToken(accessToken.getValue());
            userRepository.delete(user);
            return new ResponseEntity(user, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
    }

    // Function for retrieving todo and event descriptions of a remote CalDAV calendar
    @GetMapping("/caldav")
    public ResponseEntity getCaldavDescriptions(Authentication authentication)
    {
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        List<String> descriptions = new ArrayList<String>();
        if (user != null)
        {
            try
            {
                Sardine sardine = SardineFactory.begin();
                InputStream calendarFile = sardine.get(user.getCaldavAddress());
                List<ICalendar> calendars = Biweekly.parse(calendarFile).all();
                for (ICalendar calendar : calendars)
                {
                    List<VEvent> events = calendar.getEvents();
                    List<VTodo> todos = calendar.getTodos();
                    ICalDate todayDate = new ICalDate(new DateTimeComponents(new Date()), true);
                    int todayYear = todayDate.getRawComponents().getYear();
                    int todayMonth = todayDate.getRawComponents().getMonth();
                    int todayDay = todayDate.getRawComponents().getDate();
                    for (VTodo todo : todos)
                    {
                        ICalDate todoStartDate = todo.getDateStart().getValue();
                        int todoStartYear = todoStartDate.getRawComponents().getYear();
                        int todoStartMonth = todoStartDate.getRawComponents().getMonth();
                        int todoStartDay = todoStartDate.getRawComponents().getDate();
                        ICalDate todoDueDate = todo.getDateDue().getValue();
                        int todoDueYear = todoDueDate.getRawComponents().getYear();
                        int todoDueMonth = todoDueDate.getRawComponents().getMonth();
                        int todoDueDay = todoDueDate.getRawComponents().getDate();
                        if (((todoStartDay <= todayDay && todoStartMonth == todayMonth && todoStartYear == todayYear) || (todoStartMonth < todayMonth && todoStartYear == todayYear) || todoStartYear < todayYear) && ((todoDueDay >= todayDay && todoStartMonth == todayMonth && todoStartYear == todayYear) || (todoDueMonth > todayMonth && todoDueYear == todayYear) || todoDueYear > todayYear))
                        {
                            descriptions.add(todo.getSummary().getValue());
                        }
                    }
                    for (VEvent event : events)
                    {
                        ICalDate eventStartDate = event.getDateStart().getValue();
                        int eventStartYear = eventStartDate.getRawComponents().getYear();
                        int eventStartMonth = eventStartDate.getRawComponents().getMonth();
                        int eventStartDay = eventStartDate.getRawComponents().getDate();
                        ICalDate eventEndDate = event.getDateEnd().getValue();
                        int eventEndYear = eventEndDate.getRawComponents().getYear();
                        int eventEndMonth = eventEndDate.getRawComponents().getMonth();
                        int eventEndDay = eventEndDate.getRawComponents().getDate();
                        if (((eventStartDay <= todayDay && eventStartMonth == todayMonth && eventStartYear == todayYear) || (eventStartMonth < todayMonth && eventStartYear == todayYear) || eventStartYear < todayYear) && ((eventEndDay >= todayDay && eventStartMonth == todayMonth && eventStartYear == todayYear) || (eventEndMonth > todayMonth && eventEndYear == todayYear) || eventEndYear > todayYear))
                        {
                            descriptions.add(event.getSummary().getValue());
                        }
                    }
                }
            }
            catch (Exception exception)
            {
                return new ResponseEntity("User not found or fetching failed", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity(descriptions, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity("User not found or fetching failed", HttpStatus.NOT_FOUND);
        }
    }

    // Function for retrieving all timers of an user
    @GetMapping("/timers")
    public ResponseEntity getAllTimersOfUser(Authentication authentication)
    {
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        if (user != null)
        {
            return new ResponseEntity(timerRepository.findByUserId(user.getId()), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
    }

    // Function for creating a new timer of an user
    @PostMapping("/timers")
    public ResponseEntity createTimer(Authentication authentication, @RequestBody Timer timer)
    {
        if (timerHasDescription(timer))
        {
            User user = userRepository.findByUsername(authentication.getName()).orElse(null);
            if (user != null)
            {
                timer.setUser(user);
                timer.setIsBreak(false);
                timer.setDuration(user.getWorkDuration());
                Timer returnValue = timerRepository.save(timer);
                Timer breakTimer = new Timer(user, returnValue, true, SHORT_BREAK_DESCRIPTION, user.getShortBreakDuration());
                if ((timerRepository.findByUserId(user.getId()).size() + 1) % (user.getLongBreakRatio() * 2) == 0)
                {
                    breakTimer.setDescription(LONG_BREAK_DESCRIPTION);
                    breakTimer.setDuration(user.getLongBreakDuration());
                }
                timerRepository.save(breakTimer);
                return new ResponseEntity(returnValue, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity("User not found or invalid description", HttpStatus.NOT_FOUND);
            }
        }
        else
        {
            return new ResponseEntity("User not found or invalid description", HttpStatus.NOT_FOUND);
        }

    }

    // Function for retrieving a timer of an user
    @GetMapping("timers/{id}")
    public ResponseEntity getTimer(Authentication authentication, @PathVariable Long id)
    {
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        Timer timer = timerRepository.findByUserIdAndId(user.getId(), id).orElse(null);
        if (timer != null)
        {
            return new ResponseEntity(timer, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity("User and/or timer not found", HttpStatus.NOT_FOUND);
        }
    }

    // Function for deleting a timer of an user
    @DeleteMapping("timers/{id}")
    public ResponseEntity deleteTimer(Authentication authentication, @PathVariable Long id)
    {
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        Timer timer = timerRepository.findByUserIdAndId(user.getId(), id).orElse(null);
        if (timer != null && !timer.getIsBreak())
        {
            timerRepository.delete(timer);
            updateTimerDurations(user);
            return new ResponseEntity(timer, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity("User and/or timer not found or invalid timer type", HttpStatus.NOT_FOUND);
        }
    }
}
