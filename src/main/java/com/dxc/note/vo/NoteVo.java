package com.dxc.note.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteVo {

    private String groupName; // 分组名称
    private long noteCount; // 云记数量

    private Integer typeId; // 类型ID

}
