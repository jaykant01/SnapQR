import {RouterModule, Routes} from '@angular/router';
import {HeroComponent} from '../hero/hero.component';
import {LoginComponent} from '../login/login.component';
import {SignupComponent} from '../signup/signup.component';
import {DashboardComponent} from '../dashboard/dashboard.component';
import {authGuard} from '../guards/auth.guard';
import {GenerateQrComponent} from '../generate-qrcode/generate-qr.component';


export const routes: Routes = [
  { path: '', component: HeroComponent, title: 'QRCode-Service' },
  { path: 'login', component: LoginComponent, title: 'Login' },
  { path: 'oauth-success', component: LoginComponent },
  { path: 'signup', component: SignupComponent, title: 'Signup' },
  { path: 'dashboard', component: DashboardComponent, title: 'Dashboard', canActivate:[authGuard] },
  { path: 'generate', component: GenerateQrComponent, title: 'GenerateQr', canActivate:[authGuard] },
  { path: '**', redirectTo: '' }
];

