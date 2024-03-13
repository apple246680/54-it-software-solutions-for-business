using System;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Windows.Forms;
using System.Text.Json;
using System.Net.Http;
using System.Text;
using System.IO;
namespace WMLSD.UserControls
{
    public partial class Management : UserControl
    {
        public Management(bool canTr)
        {
            InitializeComponent();
            var zonetime = Enumerable.Range(-12, 27).Select(i => new { Zone = $"{i:+00;-00;+00}:00", Value = i }).ToList();
            TimeZoneComboBox.DataSource = zonetime;
            TimeZoneComboBox2.DataSource = zonetime;
            TimeZoneComboBox.SelectedIndex = 12;
            TimeZoneComboBox2.SelectedIndex = 12;
            if (canTr)
            {
                tabControl1.SelectedIndex = Properties.Settings.Default.PageIndex;
            }
        }

        private void SearchButton_Click(object sender, EventArgs e)
        {
            OrderPanel.Visible = false;
            TicketPanel.Visible = false;
            var entities = new Entities();
            var account = entities.Accounts.ToList();
            if (!string.IsNullOrWhiteSpace(VIsitorNameTextBox.Text))
            {
                account = account.Where(x => x.FullName.Contains(VIsitorNameTextBox.Text)).ToList();
            }
            if (StatusCheckBox.Checked)
            {
                account = account.Where(x => x.Status == true).ToList();
            }
            var data = account.Select(x => new
            {
                x.ID,
                x.FullName,
                Status = x.Status ? "正常" : "停用",
                UpDate = x.UpDateTime.HasValue ? ((DateTimeOffset)(x.UpDateTime.Value.ToUniversalTime())).ToOffset(TimeSpan.FromHours((int)TimeZoneComboBox.SelectedValue)).ToString("yyyy-MM-dd HH:mm:ss") : "未曾更新",
                CanOrder = x.Orders.Count(y => y.PayTime.HasValue),
                canTotalTicket = x.Orders.Where(y => y.PayTime.HasValue).Sum(y => y.Tickets.Count),
                canUseTicket = x.Orders.Where(y => y.PayTime.HasValue).Sum(y => y.Tickets.Count(z => z.EndTime.HasValue ? z.EndTime.Value > DateTime.Now : true))
            }).ToList();
            AccountDataGridView.Rows.Clear();
            data.ForEach(x =>
            {
                AccountDataGridView.Rows.Add(x.ID, x.FullName, x.Status, x.UpDate, x.CanOrder, x.canTotalTicket, x.canUseTicket, "查看");
            });
        }
        Account selectacc;
        private void AccountDataGridView_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.ColumnIndex == 7 && e.RowIndex != -1)
            {
                var selectacc = new Entities().Accounts.Find((Guid)AccountDataGridView.Rows[e.RowIndex].Cells[0].Value);
                StopAccountLabel.Text = selectacc.Status? "停用帳號":"啟用帳號";
                FullNameLabel.Text = $"全名:{selectacc.FullName}(Login ID:{selectacc.Account1})-";
                var data = selectacc.Orders.Select(x => new
                {
                    OrderID = x.ID,
                    OrderPrice = x.Tickets.Sum(y => y.PassType.Price),
                    Orderdel = x.Tickets.Sum(y => y.PassType.Price - (y.PassType.Price * y.PassAge.Discount)),
                    OrderRealPrice = x.Tickets.Sum(y => y.PassType.Price * y.PassAge.Discount),
                    PayTime = x.PayTime.HasValue ? ((DateTimeOffset)(x.PayTime.Value.ToUniversalTime())).ToOffset(TimeSpan.FromHours((int)TimeZoneComboBox.SelectedValue)).ToString("yyyy-MM-dd HH:mm:ss") : "尚未付款",
                    CreatedTime = ((DateTimeOffset)(x.CreatedTime.ToUniversalTime())).ToOffset(TimeSpan.FromHours((int)TimeZoneComboBox.SelectedValue)).ToString("yyyy-MM-dd HH:mm:ss"),
                    TicketCount = x.Tickets.Count
                }).ToList();
                OrderDataGridView.Rows.Clear();
                data.ForEach(x =>
                {
                    OrderDataGridView.Rows.Add(x.OrderID, x.OrderPrice, x.Orderdel, x.OrderRealPrice, x.PayTime, x.CreatedTime, x.TicketCount);
                });
                OrderPanel.Visible = true;
            }
        }

        private void StopAccountLabel_Click(object sender, EventArgs e)
        {
            var entities = new Entities();
            var acc = entities.Accounts.Find(selectacc.ID);
            acc.Status = !acc.Status;
            entities.SaveChanges();
            entities = new Entities();
            var account = entities.Accounts.ToList();
            if (!string.IsNullOrWhiteSpace(VIsitorNameTextBox.Text))
            {
                account = account.Where(x => x.FullName.Contains(VIsitorNameTextBox.Text)).ToList();
            }
            if (StatusCheckBox.Checked)
            {
                account = account.Where(x => x.Status == true).ToList();
            }
            var data = account.Select(x => new
            {
                x.ID,
                x.FullName,
                Status = x.Status ? "正常" : "停用",
                UpDate = x.UpDateTime.HasValue ? ((DateTimeOffset)(x.UpDateTime.Value.ToUniversalTime())).ToOffset(TimeSpan.FromHours((int)TimeZoneComboBox.SelectedValue)).ToString("yyyy-MM-dd HH:mm:ss") : "未曾更新",
                CanOrder = x.Orders.Count(y => y.PayTime.HasValue),
                canTotalTicket = x.Orders.Where(y => y.PayTime.HasValue).Sum(y => y.Tickets.Count),
                canUseTicket = x.Orders.Where(y => y.PayTime.HasValue).Sum(y => y.Tickets.Count(z => z.EndTime.HasValue ? z.EndTime.Value > DateTime.Now : true))
            }).ToList();
            data.ForEach(x =>
            {
                AccountDataGridView.Rows.Add(x.ID, x.FullName, x.Status, x.UpDate, x.CanOrder, x.canTotalTicket, x.canUseTicket, "查看");
            });
        }

        private void OrderDataGridView_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.ColumnIndex == 0 && e.RowIndex != -1)
            {
                var OrderID = (Guid)OrderDataGridView.Rows[e.RowIndex].Cells[0].Value;
                OrderIDLabel.Text = $"訂單編號:{OrderID}";
                var Order = new Entities().Orders.Find(OrderID);
                var data = Order.Tickets.OrderBy(x => x.OrderID).Select((x, y) => new
                {
                    ID = x.ID + $"-{(y + 1).ToString("000")}",
                    Name = string.IsNullOrWhiteSpace(x.Name) ? "不記名" : x.Name,
                    StartTime = x.StartTime.HasValue ? ((DateTimeOffset)(x.StartTime.Value.ToUniversalTime())).ToOffset(TimeSpan.FromHours((int)TimeZoneComboBox.SelectedValue)).ToString("yyyy-MM-dd HH:mm:ss") : "尚未啟用",
                    EndTime = x.EndTime.HasValue ? ((DateTimeOffset)(x.EndTime.Value.ToUniversalTime())).ToOffset(TimeSpan.FromHours((int)TimeZoneComboBox.SelectedValue)).ToString("yyyy-MM-dd HH:mm:ss") : "尚未啟用",
                    TicketAge = x.PassAge.Description,
                    TicketType = x.PassType.Name
                }).ToList();
                var aa = new Bitmap(@"C:\Users\USER\Downloads\Untitled.png");
                TicketDataGridView.Rows.Clear();
                data.ForEach(x =>
                {
                    TicketDataGridView.Rows.Add(x.ID, x.Name, x.StartTime, x.EndTime, x.TicketAge, x.TicketType, aa);
                });
                TicketPanel.Visible = true;
            }
        }

        private void TicketDataGridView_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex != -1 && e.ColumnIndex == 6)
            {
                var find = (string)TicketDataGridView.Rows[e.RowIndex].Cells[0].Value;
                var postdata = new StringContent(JsonSerializer.Serialize(new
                {
                    Text = find,
                    SizePixels = 64,
                    CorrectionLevel = 0
                }), Encoding.UTF8, "application/json");
                var Response = new HttpClient().PostAsync("http://localhost:5000/Image/GenerateQRCode", postdata).Result;
                if (Response.IsSuccessStatusCode)
                {
                    var imageBytes = Response.Content.ReadAsByteArrayAsync().Result;
                    var qr = new Bitmap(new MemoryStream(imageBytes));
                    var QRcode = new QRCode(qr) { Dock = DockStyle.Fill };
                    QRcode.ShowDialog();
                }
            }
        }

        private void CalButton_Click(object sender, EventArgs e)
        {
            var entities = new Entities();
            var Start = StartDateTimePicker.Value.Date;
            var End = EndDateTimePicker.Value.Date;
            int timeZoneOffsetHours = (int)TimeZoneComboBox2.SelectedValue;
            var StartDate = new DateTimeOffset(Start.Year, Start.Month, Start.Day, 0, 0, 0, TimeSpan.FromHours(timeZoneOffsetHours));
            var EndDate = new DateTimeOffset(End.Year, End.Month, End.Day, 0, 0, 0, TimeSpan.FromHours(timeZoneOffsetHours));
            label9.Text = $"計算區間(起): {StartDate.ToString("yyyy/MM/dd HH:mm:ss zzz")}";
            label10.Text = $"計算區間(迄):{EndDate.ToString("yyyy/MM/dd HH:mm:ss zzz")}";
            var va = entities.TicketWIthMuseums.Where(x => x.Ticket.Order.PayTime >= StartDate && x.Ticket.Order.PayTime <= EndDate);
            var totalPrice = va.Any() ? (decimal)va.Sum(x => (x.Ticket.PassAge.Discount * x.Ticket.PassType.Price)) : 0;
            label8.Text = $"區間票面總額:{totalPrice}EUR";
            var GridData = entities.Museums.Where(x=>x.TicketWIthMuseums.Any(y=>y.Ticket.Order.PayTime>=StartDate&&y.Ticket.Order.PayTime<=EndDate)).ToList().Select(x =>
            {
                var price = x.TicketWIthMuseums.GroupBy(twm => twm.Ticket).Select(g =>
                {
                    var ticket = g.Key;
                    var discount = ticket.PassAge.Discount;
                    var basePrice = ticket.PassType.Price;
                    var aaprice = basePrice * discount;
                    var ticketCountForThisMuseum = g.Count();
                    if (ticketCountForThisMuseum==0)
                    {
                        return 0;
                    }
                    var totalTicketCount = x.TicketWIthMuseums.Count(twm => twm.TicketID == ticket.ID);
                    return ((aaprice-(aaprice*(decimal)0.03)) * (decimal)(ticketCountForThisMuseum / (decimal)totalTicketCount));

                }).Sum();
                return new
                {
                    OSMID = x.ID,
                    x.Name,
                    Price = price
                };
            }).ToList();
            label11.Text = $"WML區間手續費:{totalPrice-GridData.Sum(x=>x.Price)}EUR";
            label12.Text = $"聯盟區間分潤總額:{GridData.Sum(x => x.Price)}EUR";
            MuseumDataGridView.Rows.Clear();
            GridData.ForEach(x =>
            {
                MuseumDataGridView.Rows.Add(x.OSMID,x.Name,x.Price);
            });
        }

        private void SaveButton_Click(object sender, EventArgs e)
        {
            var sfd = new SaveFileDialog();
            sfd.Filter = "CSV files (*.csv)|*.csv|All files (*.*)|*.*";
            sfd.FilterIndex = 1;
            sfd.RestoreDirectory = true;
            if (sfd.ShowDialog() == DialogResult.OK)
            {
                using (var sw = new StreamWriter(sfd.FileName, false,Encoding.UTF8))
                {
                    for (int i = 0; i < MuseumDataGridView.Columns.Count; i++)
                    {
                        sw.Write(MuseumDataGridView.Columns[i].HeaderText);
                        if (i < MuseumDataGridView.Columns.Count - 1)
                            sw.Write(",");
                    }
                    sw.WriteLine();
                    foreach (DataGridViewRow row in MuseumDataGridView.Rows)
                    {
                        if (!row.IsNewRow)
                        {
                            for (int i = 0; i < row.Cells.Count; i++)
                            {
                                sw.Write(row.Cells[i].Value?.ToString());
                                if (i < row.Cells.Count - 1)
                                    sw.Write(",");
                            }
                            sw.WriteLine();
                        }
                    }
                }
            }
        }

        private void tabControl1_SelectedIndexChanged(object sender, EventArgs e)
        {
            Properties.Settings.Default.PageIndex=tabControl1.SelectedIndex;
            Properties.Settings.Default.Save();
        }
    }
}