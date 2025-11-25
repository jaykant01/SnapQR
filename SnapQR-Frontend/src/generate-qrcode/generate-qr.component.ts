import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {QrService} from '../services/qr.service';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-generate-qrcode',
  imports: [
    ReactiveFormsModule,
    NgForOf,
    NgIf
  ],
  templateUrl: './generate-qr.component.html',
  styleUrl: './generate-qr.component.css'
})
export class GenerateQrComponent {
  form: FormGroup;
  loading = false;
  error: string | null = null;
  previewUrl: string | null = null;

  private lastObjectUrl: string | null = null;

  // Content types → defines WHAT user is encoding
  contentTypes = [
    { value: 'URL', label: 'URL' },
    { value: 'TEXT', label: 'Text' },
    { value: 'WIFI', label: 'Wi-Fi' },
    { value: 'CONTACT', label: 'Contact (vCard)' }
  ];

  // Image types → defines WHAT image format backend returns
  imageTypes = [
    { value: 'PNG', label: 'PNG' },
    { value: 'JPEG', label: 'JPEG/JPG' },
    { value: 'GIF', label: 'GIF' }
  ];

  constructor(private fb: FormBuilder, private qrService: QrService) {
    this.form = this.fb.group({
      contentType: ['URL', Validators.required],
      imageType: ['PNG', Validators.required],

      content: [''],

      wifi_ssid: [''],
      wifi_password: [''],
      wifi_encryption: ['WPA'],

      contact_name: [''],
      contact_phone: [''],
      contact_email: [''],
      contact_company: [''],
      contact_title: ['']
    });

    this.setTypeWatchers();
  }

  private setTypeWatchers() {
    this.form.get('contentType')?.valueChanges.subscribe(type => {
      this.error = null;
      this.previewUrl = null;

      this.form.get('content')?.clearValidators();
      this.form.get('wifi_ssid')?.clearValidators();
      this.form.get('contact_name')?.clearValidators();

      if (type === 'URL') {
        this.form.get('content')?.setValidators([
          Validators.required,
          Validators.pattern(/https?:\/\/.+/i)
        ]);
      }

      if (type === 'TEXT') {
        this.form.get('content')?.setValidators([Validators.required]);
      }

      if (type === 'WIFI') {
        this.form.get('wifi_ssid')?.setValidators([Validators.required]);
      }

      if (type === 'CONTACT') {
        this.form.get('contact_name')?.setValidators([Validators.required]);
      }

      this.form.get('content')?.updateValueAndValidity();
      this.form.get('wifi_ssid')?.updateValueAndValidity();
      this.form.get('contact_name')?.updateValueAndValidity();
    });
  }

  private buildContent(): string {
    const type = this.form.get('contentType')?.value;

    if (type === 'URL' || type === 'TEXT') {
      return this.form.get('content')?.value || '';
    }

    if (type === 'WIFI') {
      const ssid = this.form.get('wifi_ssid')?.value || '';
      const pass = this.form.get('wifi_password')?.value || '';
      const enc  = this.form.get('wifi_encryption')?.value || 'WPA';
      return `WIFI:S:${escapeSemicolons(ssid)};T:${enc};P:${escapeSemicolons(pass)};;`;
    }

    if (type === 'CONTACT') {
      const name = this.form.get('contact_name')?.value || '';
      const phone = this.form.get('contact_phone')?.value || '';
      const email = this.form.get('contact_email')?.value || '';
      const company = this.form.get('contact_company')?.value || '';
      const title = this.form.get('contact_title')?.value || '';

      const lines = [
        'BEGIN:VCARD',
        'VERSION:3.0',
        `FN:${escapeNewlines(name)}`,
      ];
      if (company) lines.push(`ORG:${escapeNewlines(company)}`);
      if (title) lines.push(`TITLE:${escapeNewlines(title)}`);
      if (phone) lines.push(`TEL;TYPE=CELL:${phone}`);
      if (email) lines.push(`EMAIL:${email}`);
      lines.push('END:VCARD');

      return lines.join('\n');
    }

    return this.form.get('content')?.value || '';
  }

  generate() {
    this.error = null;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.error = 'Please fill required fields.';
      return;
    }

    const payload = {
      content: this.buildContent(),
      type: this.form.get('imageType')?.value
    };

    this.loading = true;

    this.qrService.generateQr(payload).subscribe({
      next: (blob: Blob) => {
        const url = URL.createObjectURL(blob);
        this.setPreviewUrl(url);
      },
      error: (err) => {
        console.error(err);
        this.error = 'Failed to generate QR code.';
      },
      complete: () => this.loading = false
    });
  }

  private setPreviewUrl(url: string) {
    if (this.lastObjectUrl) {
      try { URL.revokeObjectURL(this.lastObjectUrl); } catch {}
    }
    this.previewUrl = url;
    this.lastObjectUrl = url;
  }

  downloadPreview(filename = 'qrcode.png') {
    if (!this.previewUrl) return;
    const a = document.createElement('a');
    a.href = this.previewUrl;
    a.download = filename;
    a.click();
  }
}

function escapeSemicolons(s: string) {
  return (s || '').replace(/;/g, '\\;');
}
function escapeNewlines(s: string) {
  return (s || '').replace(/\n/g, '\\n');
}
