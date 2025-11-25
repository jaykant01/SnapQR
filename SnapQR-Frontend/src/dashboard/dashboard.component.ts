import {Component, OnInit} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {QrService} from '../services/qr.service';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {AuthService} from '../services/auth.service';

@Component({
  selector: 'app-dashboard',
  imports: [
    RouterLink,
    NgIf,
    DatePipe,
    NgForOf
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  // User info
  username: string = "";
  email: string = "";
  role: string = "";

  // Navbar dropdown
  menuOpen = false;

  // QR History
  qrCodes: any[] = [];
  totalQrs = 0;
  loading = false;

  constructor(
    private router: Router,
    private qrService: QrService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadUser();
    this.loadHistory();
  }

  // Load logged-in user from backend
  loadUser() {
    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        this.username = user.username;
        this.email = user.email;
        this.role = Array.isArray(user.roles)
          ? user.roles.join(', ')
          : (user.role || "USER");
      },
      error: () => {
        console.error("Failed to load user details");
      }
    });
  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']).then();
  }


  loadHistory() {
    this.qrService.getHistory().subscribe({
      next: (res: any) => {

        const list = res?.data || res || [];

        // Map backend fields to dashboard model
        this.qrCodes = list.map((qr: any) => ({
          id: qr.id,                     // UUID
          content: qr.content,
          name: qr.content?.substring(0, 20) || "QR Code",
          createdAt: qr.createdAt,
          scans: qr.scans || 0
        }));

        this.totalQrs = this.qrCodes.length;
      },
      error: () => {
        this.qrCodes = [];
        this.totalQrs = 0;
      }
    });
  }

  viewModalOpen = false;
  viewImageUrl: string | null = null;
  viewContent: string = "";
  viewType: string = "";



  viewQRCode(id: string) {
    this.viewModalOpen = true;
    this.viewImageUrl = null;

    const qr = this.qrCodes.find((q: any) => q.id === id);
    if (qr) {
      this.viewContent = qr.content || "N/A";
      this.viewType = qr.type || "PNG";
    }

    this.qrService.downloadById(id).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        this.viewImageUrl = url;
      },
      error: () => {
        alert("Failed to load QR code.");
        this.viewModalOpen = false;
      }
    });
  }


  closeViewModal() {
    this.viewModalOpen = false;

    if (this.viewImageUrl) {
      window.URL.revokeObjectURL(this.viewImageUrl);
    }

    this.viewImageUrl = null;
    this.viewContent = "";
    this.viewType = "";
  }


  downloadQRCode(id: any): void {
    this.qrService.downloadById(id).subscribe({
      next: (file: Blob) => {
        const url = window.URL.createObjectURL(file);
        const a = document.createElement('a');
        a.href = url;
        a.download = `qr-${id}.png`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to download QR code.')
    });
  }

  deleteQRCode(id: string) {
    this.qrService.deleteById(id).subscribe({
      next: () => {
        this.qrCodes = this.qrCodes.filter(q => q.id !== id);
      },
      error: () => {
        // But if deletion actually happened (as now)
        this.qrCodes = this.qrCodes.filter(q => q.id !== id);
      }
    });
  }


}
