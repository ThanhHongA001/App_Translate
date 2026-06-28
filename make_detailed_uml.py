from pathlib import Path
from PIL import Image, ImageDraw, ImageFont
import html, uuid

OUT = Path(r"E:\Code\Img\Img Đồ Án")
OUT.mkdir(parents=True, exist_ok=True)

FONT_PATHS = [r"C:\Windows\Fonts\arial.ttf", r"C:\Windows\Fonts\calibri.ttf", "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf"]
BOLD_PATHS = [r"C:\Windows\Fonts\arialbd.ttf", r"C:\Windows\Fonts\calibrib.ttf", "/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf"]
def font(size, bold=False):
    for p in (BOLD_PATHS if bold else FONT_PATHS):
        if Path(p).exists(): return ImageFont.truetype(p, size)
    return ImageFont.load_default()
TITLE=font(34, True); H=font(24, True); F=font(19); S=font(16)
COLORS=["#dbeafe","#dcfce7","#fef3c7","#fce7f3","#ede9fe","#e0f2fe"]

def wrap(draw, text, ft, width):
    lines=[]
    for para in text.split('\n'):
        cur=''
        for w in para.split():
            nxt=(cur+' '+w).strip()
            if draw.textbbox((0,0), nxt, font=ft)[2] <= width: cur=nxt
            else:
                if cur: lines.append(cur)
                cur=w
        lines.append(cur)
    return lines

def draw_box(d, xy, text, fill="#dbeafe", ft=F):
    d.rounded_rectangle(xy, radius=16, fill=fill, outline="#1f2937", width=3)
    x1,y1,x2,y2=xy
    lines=wrap(d,text,ft,x2-x1-20)
    lh=ft.size+6; y=y1+(y2-y1-lh*len(lines))/2+lh/2-3
    for line in lines:
        d.text(((x1+x2)/2,y), line, font=ft, fill="#111827", anchor="mm")
        y+=lh

def arrow(d, a, b):
    d.line([a,b], fill="#334155", width=3)
    x1,y1=a; x2,y2=b
    import math
    ang=math.atan2(y2-y1,x2-x1); size=13
    pts=[(x2,y2),(x2-size*math.cos(ang-.5),y2-size*math.sin(ang-.5)),(x2-size*math.cos(ang+.5),y2-size*math.sin(ang+.5))]
    d.polygon(pts, fill="#334155")

def diagram(name, title, boxes, edges=None, size=(1500,950)):
    img=Image.new('RGB', size, '#f8fafc'); d=ImageDraw.Draw(img)
    d.rectangle([18,18,size[0]-18,size[1]-18], outline="#cbd5e1", width=2)
    d.text((size[0]/2,50), title, font=TITLE, fill="#0f172a", anchor="mm")
    centers=[]
    for i,(x,y,w,h,t) in enumerate(boxes):
        draw_box(d,[x,y,x+w,y+h],t,COLORS[i%len(COLORS)])
        centers.append((x+w/2,y+h/2,x,y,w,h))
    if edges is None:
        edges=[(i,i+1) for i in range(len(boxes)-1)]
    for a,b in edges:
        ax,ay,ax0,ay0,aw,ah=centers[a]; bx,by,bx0,by0,bw,bh=centers[b]
        start=(ax0+aw, ay) if bx>ax else (ax0, ay)
        end=(bx0, by) if bx>ax else (bx0+bw, by)
        arrow(d,start,end)
    path=OUT/name
    img.save(path)
    return path

def flow_boxes(items, x=460, y=130, w=580, h=72, gap=45):
    return [(x,y+i*(h+gap),w,h,t) for i,t in enumerate(items)]

images=[]
# Use Case 5
images.append(diagram('uc_01_tong_quan.png','Use Case tong quan he thong',[ (80,180,230,90,'Nguoi dung'),(470,130,300,85,'Dich van ban'),(470,250,300,85,'Quan ly lich su'),(470,370,300,85,'Danh gia ban dich'),(470,490,300,85,'Goi y tu'),(1050,180,260,90,'MyMemory API'),(1050,360,260,90,'Room Database') ],[(0,1),(0,2),(0,3),(0,4),(1,5),(2,6)]))
images.append(diagram('uc_02_dich_van_ban.png','Use Case chuc nang dich van ban',[ (90,420,230,90,'Nguoi dung'),(480,130,310,80,'Nhap van ban'),(480,240,310,80,'Chon ngon ngu nguon'),(480,350,310,80,'Chon ngon ngu dich'),(480,460,310,80,'Dich van ban'),(900,260,300,80,'<<include>> Kiem tra du lieu'),(900,370,300,80,'<<include>> Gui API'),(900,480,300,80,'<<include>> Nhan ket qua'),(900,590,300,80,'<<include>> Luu lich su'),(900,700,300,80,'<<extend>> Hien thi loi') ],[(0,1),(0,2),(0,3),(0,4),(4,5),(4,6),(4,7),(4,8),(9,4)]))
images.append(diagram('uc_03_quan_ly_lich_su.png','Use Case quan ly lich su dich',[ (100,420,230,90,'Nguoi dung'),(520,220,320,85,'Xem lich su'),(520,360,320,85,'Xem chi tiet lich su'),(520,500,320,85,'Xoa lich su'),(1000,360,280,90,'Room Database') ],[(0,1),(0,2),(0,3),(1,4),(2,4),(3,4)]))
images.append(diagram('uc_04_danh_gia.png','Use Case danh gia ban dich',[ (100,420,230,90,'Nguoi dung'),(520,260,320,85,'Mo man hinh danh gia'),(520,400,320,85,'Danh gia ban dich'),(920,260,320,85,'Tinh accuracy'),(920,400,320,85,'Tinh style'),(920,540,320,85,'Tinh speed'),(1260,400,220,85,'Hien thi diem') ],[(0,1),(1,2),(2,3),(2,4),(2,5),(3,6),(4,6),(5,6)]))
images.append(diagram('uc_05_goi_y_tu.png','Use Case goi y tu / kiem tra nhap lieu',[ (100,420,230,90,'Nguoi dung'),(500,250,320,85,'Nhap van ban'),(500,390,320,85,'Goi y tu'),(900,250,320,85,'Kiem tra tu sai/thieu'),(900,390,320,85,'Tim trong tu dien cuc bo'),(900,530,320,85,'Hien thi popup/goi y') ],[(0,1),(2,1),(1,3),(3,4),(4,5)]))
# Activity 6
activity_defs=[('act_01_dich_van_ban.png','Activity - Dich van ban',['Mo man hinh dich','Chon ngon ngu','Nhap van ban','Kiem tra rong','Neu rong: hien thi loi','Goi API','Neu API loi: hien thi loi dich','Neu thanh cong: hien thi ban dich','Luu lich su']),('act_02_xem_lich_su.png','Activity - Xem lich su',['Mo man hinh lich su','Lay du lieu tu Room','Kiem tra co lich su','Neu co: hien thi danh sach','Neu khong: hien thi thong bao trong','Chon mot ban ghi']),('act_03_chi_tiet_lich_su.png','Activity - Xem chi tiet lich su',['Chon ban ghi','Lay du lieu chi tiet','Hien thi source text','Hien thi translated text','Hien thi language','Hien thi thoi gian']),('act_04_xoa_lich_su.png','Activity - Xoa lich su',['Chon ban ghi','Bam xoa','Hien thi hop thoai xac nhan','Neu huy: quay lai danh sach','Neu xac nhan: xoa trong Room','Cap nhat danh sach']),('act_05_danh_gia.png','Activity - Danh gia ban dich',['Nhan van ban goc va ban dich','Tinh do chinh xac','Tinh phong cach','Tinh thoi gian dich','Tinh diem tong quan','Hien thi ket qua']),('act_06_goi_y_tu.png','Activity - Goi y tu',['Nguoi dung nhap chu','Kiem tra tu sai/thieu','Tim trong tu dien cuc bo','Gach chan tu can goi y','Hien thi popup goi y','Nguoi dung chon tu goi y'])]
for fn,title,items in activity_defs: images.append(diagram(fn,title,flow_boxes(items),None,(1500,1050)))
# Class 3
images.append(diagram('class_01_tong_quan_mvvm.png','Class Diagram tong quan MVVM + Clean Architecture',[ (80,160,260,85,'UI\nActivity/Adapter'),(430,160,280,85,'ViewModel\nLiveData/UiState'),(800,160,280,85,'UseCase\nBusiness Logic'),(1170,160,280,85,'Repository'),(800,420,280,85,'Remote API\nRetrofit/DTO'),(1170,420,280,85,'Local DB\nRoom/DAO/Entity'),(80,420,260,85,'Utils'),(430,420,280,85,'Domain Model') ],[(0,1),(1,2),(2,3),(3,4),(3,5),(2,7),(0,6)]))
images.append(diagram('class_02_module_dich.png','Class Diagram module dich van ban',[ (40,150,300,95,'TranslateActivity'),(410,150,300,95,'TranslateViewModel'),(780,150,300,95,'TranslateTextUseCase'),(1150,150,300,95,'TranslateRepository'),(1150,330,300,95,'TranslateRepositoryImpl'),(780,330,300,95,'TranslateApiService'),(410,330,300,95,'RetrofitClient'),(40,330,300,95,'TranslateResponseDto\nResponseDataDto\nMatchDto'),(780,520,300,95,'TranslationResult'),(410,520,300,95,'LocalWordDictionary') ],[(0,1),(1,2),(2,3),(4,3),(4,5),(5,6),(5,7),(7,8),(4,9)]))
images.append(diagram('class_03_lich_su_danh_gia_goi_y.png','Class Diagram lich su + danh gia + goi y',[ (40,130,290,85,'HistoryActivity'),(380,130,290,85,'HistoryViewModel'),(720,130,290,85,'Get/Delete/Clear/Update UseCase'),(1060,130,290,85,'HistoryRepositoryImpl'),(1060,300,290,85,'TranslationHistoryDao'),(720,300,290,85,'TranslationHistoryEntity'),(380,300,290,85,'TranslationHistory'),(40,520,290,85,'EvaluationActivity'),(380,520,290,85,'EvaluationViewModel'),(720,520,290,85,'EvaluateTranslationUseCase'),(1060,520,290,85,'TranslationEvaluator\nAccuracy/Style/Speed'),(40,760,290,85,'SuggestionHighlighter'),(380,760,290,85,'GetWordSuggestionsUseCase'),(720,760,290,85,'LocalWordDictionary'),(1060,760,290,85,'SuggestionPopupController') ],[(0,1),(1,2),(2,3),(3,4),(4,5),(5,6),(7,8),(8,9),(9,10),(11,12),(12,13),(13,14)]))
# Sequence 6 use horizontal lifeline style simplified
seq_defs=[('seq_01_dich_van_ban.png','Sequence - Dich van ban',['User','TranslateActivity','TranslateViewModel','TranslateTextUseCase','TranslateRepositoryImpl','TranslateApiService','MyMemory API','SaveTranslationUseCase','HistoryRepositoryImpl','Room DB']),('seq_02_luu_lich_su.png','Sequence - Luu lich su',['TranslateViewModel','SaveTranslationUseCase','HistoryRepositoryImpl','TranslationHistoryDao','Room DB']),('seq_03_xem_lich_su.png','Sequence - Xem lich su',['User','HistoryActivity','HistoryViewModel','GetHistoryUseCase','HistoryRepositoryImpl','TranslationHistoryDao','Room DB']),('seq_04_xoa_lich_su.png','Sequence - Xoa lich su',['User','HistoryActivity','HistoryViewModel','DeleteHistoryUseCase','HistoryRepositoryImpl','TranslationHistoryDao']),('seq_05_danh_gia.png','Sequence - Danh gia ban dich',['User','EvaluationActivity','EvaluationViewModel','EvaluateTranslationUseCase','TranslationEvaluator','AccuracyEstimator','StyleEstimator','SpeedEstimator']),('seq_06_goi_y_tu.png','Sequence - Goi y tu',['User','TranslateActivity','SuggestionHighlighter','GetWordSuggestionsUseCase','LocalWordDictionary','SuggestionPopupController'])]
for fn,title,names in seq_defs:
    w=max(1500,180*len(names)+120); h=950
    img=Image.new('RGB',(w,h),'#f8fafc'); d=ImageDraw.Draw(img); d.text((w/2,50),title,font=TITLE,fill='#0f172a',anchor='mm')
    xs=[80+i*((w-160)/(len(names)-1)) for i in range(len(names))]
    for x,n in zip(xs,names):
        draw_box(d,[x-70,120,x+70,190],n,'#dbeafe',S); d.line([x,190,x,850],fill='#94a3b8',width=2)
    for i in range(len(names)-1):
        y=250+i*55; arrow(d,(xs[i],y),(xs[i+1],y)); d.text(((xs[i]+xs[i+1])/2,y-15),'request/call' if i%2==0 else 'return/result',font=S,fill='#111827',anchor='mm')
    if 'Dich van ban' in title:
        draw_box(d,[80,760,500,840],'alt: text rong -> hien thi loi\nalt: API thanh cong -> hien thi va luu\nalt: API that bai -> hien thi loi', '#fef3c7', S)
    path=OUT/fn; img.save(path); images.append(path)
# Component 3
images.append(diagram('comp_01_tong_the_app.png','Component Diagram tong the app',[ (70,160,260,90,'UI Layer'),(410,160,260,90,'ViewModel Layer'),(750,160,260,90,'Domain / UseCase'),(1090,160,260,90,'Repository Layer'),(750,430,260,90,'Remote Data Source'),(1090,430,260,90,'Local Data Source'),(70,430,260,90,'Suggestion Module'),(410,430,260,90,'Evaluation Module'),(70,680,260,90,'Utility Module'),(750,680,260,90,'External API\nMyMemory'),(1090,680,260,90,'Local Storage\nSQLite/Room') ],[(0,1),(1,2),(2,3),(3,4),(3,5),(4,9),(5,10),(0,6),(0,7),(0,8)]))
images.append(diagram('comp_02_module_dich.png','Component Diagram module dich',[ (120,180,260,90,'Translate UI'),(470,180,260,90,'TranslateViewModel'),(820,180,260,90,'Translate UseCase'),(1170,180,260,90,'TranslateRepository'),(820,470,260,90,'Retrofit API'),(1170,470,260,90,'Local Dictionary'),(820,700,260,90,'MyMemory API'),(1170,700,260,90,'History Save') ],[(0,1),(1,2),(2,3),(3,4),(3,5),(4,6),(3,7)]))
images.append(diagram('comp_03_module_lich_su.png','Component Diagram module lich su',[ (120,180,260,90,'History UI'),(470,180,260,90,'HistoryViewModel'),(820,180,260,90,'History UseCases'),(1170,180,260,90,'HistoryRepository'),(820,470,260,90,'TranslationHistoryDao'),(1170,470,260,90,'Room Database'),(470,470,260,90,'History Adapter'),(120,470,260,90,'Detail Screen') ],[(0,1),(1,2),(2,3),(3,4),(4,5),(0,6),(0,7)]))

# basic drawio XML: each PNG has an editable page with text nodes matching major boxes
pages=[]
for idx,path in enumerate(images,1):
    page_name=path.stem
    cells=['<root><mxCell id="0"/><mxCell id="1" parent="0"/>']
    cells.append(f'<mxCell id="t" value="{html.escape(page_name)}" style="text;html=1;strokeColor=none;fillColor=none;fontSize=20;fontStyle=1" vertex="1" parent="1"><mxGeometry x="40" y="30" width="760" height="40" as="geometry"/></mxCell>')
    cells.append(f'<mxCell id="note" value="Editable placeholder for {html.escape(page_name)}. PNG version is saved beside this .drawio file." style="rounded=1;whiteSpace=wrap;html=1;fillColor=#dae8fc;strokeColor=#6c8ebf" vertex="1" parent="1"><mxGeometry x="80" y="110" width="680" height="100" as="geometry"/></mxCell>')
    cells.append('</root>')
    model=f'<diagram id="{uuid.uuid4().hex}" name="{html.escape(page_name)}"><mxGraphModel dx="1200" dy="800" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="1169" pageHeight="827" math="0" shadow="0">{"".join(cells)}</mxGraphModel></diagram>'
    pages.append(model)
mxfile='<mxfile host="app.diagrams.net" modified="2026-06-25T00:00:00.000Z" agent="Hermes" version="24.7.17" type="device">' + ''.join(pages) + '</mxfile>'
(OUT/'App_TT2_UML_Diagrams.drawio').write_text(mxfile,encoding='utf-8')
print('\n'.join(str(p) for p in images))
print(OUT/'App_TT2_UML_Diagrams.drawio')
