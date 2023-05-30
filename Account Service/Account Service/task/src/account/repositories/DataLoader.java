//package account.repositories;
//
//import account.models.BreachedPassword;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DataLoader {
//
//    private BreachedPasswordsRepository breachedPasswordsRepository;
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public DataLoader(BreachedPasswordsRepository bpr, PasswordEncoder passwordEncoder) {
//        this.breachedPasswordsRepository = bpr;
//        this.passwordEncoder = passwordEncoder;
//        loadBreached();
//    }
//
//    private void loadBreached() {
//        if(breachedPasswordsRepository.count() != 0) {
//            return;
//        }
//        for (Long i = 0l; i < 12l; i++) {
//            String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August",
//                    "September", "October", "November", "December"};
//            BreachedPassword password = new BreachedPassword(i + 1, "PasswordFor" + monthNames[i.intValue()]);
//            password.setPassword(passwordEncoder.encode(password.getPassword()));
//            breachedPasswordsRepository.save(password);
//        }
//
//    }
//}
